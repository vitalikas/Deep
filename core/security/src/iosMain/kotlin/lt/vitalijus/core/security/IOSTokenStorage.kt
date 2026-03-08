package lt.vitalijus.core.security

import co.touchlab.kermit.Logger
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import lt.vitalijus.core.domain.util.Result
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
 * iOS implementation of TokenStorage using Keychain Services.
 *
 * Keychain provides hardware-backed encryption on modern iOS devices.
 */
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
class IOSTokenStorage(
    private val logger: Logger
) : TokenStorage {

    private val service = "lt.vitalijus.deep.tokenstorage"
    private val account = "auth_token"

    override suspend fun hasToken(): Result<Boolean, StorageError> {
        return try {
            val query = createQueryDictionary()

            val returnDataKey = CFStringCreateWithCString(kCFAllocatorDefault, kSecReturnData, null)
            val returnDataVal = CFBooleanFalse
            CFDictionaryAddValue(query, returnDataKey, returnDataVal)

            val limitKey = CFStringCreateWithCString(kCFAllocatorDefault, kSecMatchLimit, null)
            val limitVal = CFStringCreateWithCString(kCFAllocatorDefault, kSecMatchLimitOne, null)
            CFDictionaryAddValue(query, limitKey, limitVal)

            val exists = memScoped {
                val result = alloc<CFTypeRefVar>()
                val status = SecItemCopyMatching(query, result.ptr)

                CFRelease(query)
                CFRelease(returnDataKey)
                CFRelease(limitKey)
                CFRelease(limitVal)

                status == errSecSuccess
            }
            Result.Success(exists)
        } catch (e: Exception) {
            logger.e(e) { "Failed to check token existence in Keychain" }
            Result.Failure(StorageError.IOError(e))
        }
    }

    override suspend fun saveToken(token: String): Result<Unit, StorageError> {
        return try {
            clearTokenInternal()

            val query = createQueryDictionary()

            val tokenData = token.encodeToByteArray()
            val nsData = tokenData.usePinned { pinned ->
                NSData.create(bytes = pinned.addressOf(0), length = tokenData.size.convert())
            }

            val valueData = CFStringCreateWithCString(kCFAllocatorDefault, "v_Data", null)
            CFDictionaryAddValue(query, valueData, nsData)

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
                Result.Success(Unit)
            } else {
                Result.Failure(StorageError.IOError(Exception("Keychain error: $status")))
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to save token to Keychain" }
            Result.Failure(StorageError.IOError(e))
        }
    }

    override suspend fun getToken(): Result<String, StorageError> {
        return try {
            val query = createQueryDictionary()

            val returnDataKey = CFStringCreateWithCString(kCFAllocatorDefault, kSecReturnData, null)
            val returnDataVal = CFBooleanTrue
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

                if (status == errSecSuccess) {
                    val dataRef = result.value as? NSData
                    val bytes = dataRef?.bytes?.readBytes(dataRef.length.convert())
                    val token = bytes?.let { String(it, Charsets.UTF_8) }

                    if (token != null) {
                        Result.Success(token)
                    } else {
                        Result.Failure(StorageError.DecryptionFailed(Exception("Failed to decode token")))
                    }
                } else {
                    Result.Failure(StorageError.NotFound)
                }
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to retrieve token from Keychain" }
            Result.Failure(StorageError.IOError(e))
        }
    }

    override suspend fun clearToken(): Result<Unit, StorageError> {
        return clearTokenInternal()
    }

    private fun clearTokenInternal(): Result<Unit, StorageError> {
        return try {
            val query = createQueryDictionary()
            val status = SecItemDelete(query)
            CFRelease(query)

            if (status == errSecSuccess) {
                logger.i { "Token cleared from Keychain" }
            } else {
                logger.d { "No token to clear from Keychain" }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Failed to clear token from Keychain" }
            Result.Failure(StorageError.IOError(e))
        }
    }

    private fun createQueryDictionary(): CFTypeRef {
        val query = CFDictionaryCreateMutable(
            kCFAllocatorDefault,
            3.convert(),
            null,
            null
        )

        val classKey = CFStringCreateWithCString(kCFAllocatorDefault, kSecClass, null)
        val classVal =
            CFStringCreateWithCString(kCFAllocatorDefault, kSecClassGenericPassword, null)
        CFDictionaryAddValue(query, classKey, classVal)

        val serviceKey = CFStringCreateWithCString(kCFAllocatorDefault, kSecAttrService, null)
        val serviceVal = CFStringCreateWithCString(kCFAllocatorDefault, service, null)
        CFDictionaryAddValue(query, serviceKey, serviceVal)

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
