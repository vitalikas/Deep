package lt.vitalijus.core.security

import co.touchlab.kermit.Logger
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFStringCreateWithCString
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFAllocatorDefault
import platform.Foundation.NSData
import platform.Foundation.create
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccessible
import platform.Security.kSecAttrAccessibleWhenUnlockedThisDeviceOnly
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData

/**
 * iOS implementation of SecureStorage using Keychain Services.
 *
 * Keychain provides hardware-backed encryption on modern iOS devices.
 */
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
class IOSSecureStorage(
    private val logger: Logger
) : SecureStorage {

    private val service = "lt.vitalijus.deep.securestorage"
    private val account = "auth_token"

    override suspend fun saveToken(token: String) {
        try {
            // Delete any existing token first
            clearToken()

            // Create the query dictionary
            val query = createQueryDictionary()

            // Add value
            val tokenData = token.encodeToByteArray()
            val nsData = tokenData.usePinned { pinned ->
                NSData.create(bytes = pinned.addressOf(0), length = tokenData.size.convert())
            }

            val valueData = CFStringCreateWithCString(kCFAllocatorDefault, "v_Data", null)
            CFDictionaryAddValue(query, valueData, nsData)

            // Add accessibility
            val accKey = CFStringCreateWithCString(kCFAllocatorDefault, kSecAttrAccessible, null)
            val accVal = CFStringCreateWithCString(
                kCFAllocatorDefault,
                kSecAttrAccessibleWhenUnlockedThisDeviceOnly,
                null
            )
            CFDictionaryAddValue(query, accKey, accVal)

            val status = SecItemAdd(query, null)

            CFRelease(query)
            CFRelease(valueData)
            CFRelease(accKey)
            CFRelease(accVal)

            if (status == errSecSuccess) {
                logger.i { "Token saved securely to Keychain" }
            } else {
                throw SecureStorageException("Failed to save token to Keychain, status: $status")
            }
        } catch (e: SecureStorageException) {
            throw e
        } catch (e: Exception) {
            logger.e(e) { "Failed to save token to Keychain" }
            throw SecureStorageException("Failed to save token", e)
        }
    }

    override suspend fun getToken(): String? {
        return try {
            val query = createQueryDictionary()

            // Configure to return data
            val returnDataKey = CFStringCreateWithCString(kCFAllocatorDefault, kSecReturnData, null)
            val returnDataVal = CFBooleanTrue
            CFDictionaryAddValue(query, returnDataKey, returnDataVal)

            // Limit to one result
            val limitKey = CFStringCreateWithCString(kCFAllocatorDefault, kSecMatchLimit, null)
            val limitVal = CFStringCreateWithCString(kCFAllocatorDefault, kSecMatchLimitOne, null)
            CFDictionaryAddValue(query, limitKey, limitVal)

            memScoped {
                val result = alloc<CFTypeRefVar>()
                val status = SecItemCopyMatching(query, result.ptr)

                CFRelease(query)
                CFRelease(returnDataKey)
                CFRelease(limitKey)
                CFRelease(limitVal)

                if (status == errSecSuccess) {
                    val dataRef = result.value as? NSData
                    val bytes = dataRef?.bytes?.readBytes(dataRef.length.convert())
                    bytes?.let { String(it, Charsets.UTF_8) }.also {
                        logger.d { "Token retrieved from Keychain" }
                    }
                } else {
                    // Item not found is not an error
                    logger.d { "No token found in Keychain" }
                    null
                }
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to retrieve token from Keychain" }
            throw SecureStorageException("Failed to retrieve token", e)
        }
    }

    override suspend fun clearToken() {
        try {
            val query = createQueryDictionary()
            val status = SecItemDelete(query)
            CFRelease(query)

            if (status == errSecSuccess) {
                logger.i { "Token cleared from Keychain" }
            } else {
                // Not found is also acceptable
                logger.d { "No token to clear from Keychain" }
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to clear token from Keychain" }
            throw SecureStorageException("Failed to clear token", e)
        }
    }

    override suspend fun hasToken(): Boolean {
        return try {
            val query = createQueryDictionary()

            val returnDataKey = CFStringCreateWithCString(kCFAllocatorDefault, kSecReturnData, null)
            val returnDataVal = CFBooleanFalse
            CFDictionaryAddValue(query, returnDataKey, returnDataVal)

            val limitKey = CFStringCreateWithCString(kCFAllocatorDefault, kSecMatchLimit, null)
            val limitVal = CFStringCreateWithCString(kCFAllocatorDefault, kSecMatchLimitOne, null)
            CFDictionaryAddValue(query, limitKey, limitVal)

            memScoped {
                val result = alloc<CFTypeRefVar>()
                val status = SecItemCopyMatching(query, result.ptr)

                CFRelease(query)
                CFRelease(returnDataKey)
                CFRelease(limitKey)
                CFRelease(limitVal)

                status == errSecSuccess
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to check token existence in Keychain" }
            false
        }
    }

    private fun createQueryDictionary(): CFTypeRef {
        val query = CFDictionaryCreateMutable(
            kCFAllocatorDefault,
            3.convert(),
            null,
            null
        )

        // Class: Generic password
        val classKey = CFStringCreateWithCString(kCFAllocatorDefault, kSecClass, null)
        val classVal =
            CFStringCreateWithCString(kCFAllocatorDefault, kSecClassGenericPassword, null)
        CFDictionaryAddValue(query, classKey, classVal)

        // Service identifier
        val serviceKey = CFStringCreateWithCString(kCFAllocatorDefault, kSecAttrService, null)
        val serviceVal = CFStringCreateWithCString(kCFAllocatorDefault, service, null)
        CFDictionaryAddValue(query, serviceKey, serviceVal)

        // Account identifier
        val accountKey = CFStringCreateWithCString(kCFAllocatorDefault, kSecAttrAccount, null)
        val accountVal = CFStringCreateWithCString(kCFAllocatorDefault, account, null)
        CFDictionaryAddValue(query, accountKey, accountVal)

        CFRelease(classKey)
        CFRelease(classVal)
        CFRelease(serviceKey)
        CFRelease(serviceVal)
        CFRelease(accountKey)
        CFRelease(accountVal)

        return query
    }

    private val CFBooleanTrue: CFTypeRef
        get() = platform.CoreFoundation.CFBooleanTrue as CFTypeRef

    private val CFBooleanFalse: CFTypeRef
        get() = platform.CoreFoundation.CFBooleanFalse as CFTypeRef
}
