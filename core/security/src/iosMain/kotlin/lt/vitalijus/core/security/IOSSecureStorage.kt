package lt.vitalijus.core.security

import co.touchlab.kermit.Logger
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.COpaquePointerVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.interpretCPointer
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDataCreate
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFDataRef
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFStringCreateWithCString
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreFoundation.kCFTypeDictionaryKeyCallBacks
import platform.CoreFoundation.kCFTypeDictionaryValueCallBacks
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
import platform.Security.kSecValueData

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

            memScoped {
                val query = CFDictionaryCreateMutable(
                    kCFAllocatorDefault,
                    6.convert(),
                    kCFTypeDictionaryKeyCallBacks.ptr,
                    kCFTypeDictionaryValueCallBacks.ptr
                )

                CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)

                val serviceStr = cfString(service)
                val accountStr = cfString(account)
                CFDictionaryAddValue(query, kSecAttrService, serviceStr)
                CFDictionaryAddValue(query, kSecAttrAccount, accountStr)

                // Create CFData from token bytes (avoids NSData->CFTypeRef cast issue)
                val tokenBytes = token.encodeToByteArray()
                val cfData = tokenBytes.usePinned { pinned ->
                    CFDataCreate(
                        kCFAllocatorDefault,
                        pinned.addressOf(0).reinterpret<UByteVar>(),
                        tokenBytes.size.convert()
                    )
                }
                CFDictionaryAddValue(query, kSecValueData, cfData)

                CFDictionaryAddValue(
                    query,
                    kSecAttrAccessible,
                    kSecAttrAccessibleWhenUnlockedThisDeviceOnly
                )

                val status = SecItemAdd(query, null)

                logger.d { "SecItemAdd status: $status (errSecSuccess=$errSecSuccess)" }

                CFRelease(cfData)
                CFRelease(serviceStr)
                CFRelease(accountStr)
                CFRelease(query)

                if (status == errSecSuccess) {
                    logger.i { "Token saved securely to Keychain" }
                } else {
                    throw SecureStorageException("Failed to save token to Keychain, status: $status")
                }
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
            memScoped {
                val query = CFDictionaryCreateMutable(
                    kCFAllocatorDefault,
                    5.convert(),
                    kCFTypeDictionaryKeyCallBacks.ptr,
                    kCFTypeDictionaryValueCallBacks.ptr
                )

                CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
                val serviceStr = cfString(service)
                val accountStr = cfString(account)
                CFDictionaryAddValue(query, kSecAttrService, serviceStr)
                CFDictionaryAddValue(query, kSecAttrAccount, accountStr)

                CFDictionaryAddValue(query, kSecReturnData, platform.CoreFoundation.kCFBooleanTrue)
                CFDictionaryAddValue(query, kSecMatchLimit, kSecMatchLimitOne)

                val resultPtr = alloc<COpaquePointerVar>()
                val status = SecItemCopyMatching(query, resultPtr.ptr)

                CFRelease(serviceStr)
                CFRelease(accountStr)
                CFRelease(query)

                logger.d { "SecItemCopyMatching status: $status" }

                if (status == errSecSuccess && resultPtr.value != null) {
                    // Result is CFDataRef - read bytes using CF functions
                    val cfData: CFDataRef = interpretCPointer(resultPtr.value!!.rawValue)!!
                    val length = CFDataGetLength(cfData).toInt()
                    val bytesPtr = CFDataGetBytePtr(cfData)
                    val token = bytesPtr?.readBytes(length)?.decodeToString()

                    logger.d { "Token retrieved from Keychain (length=$length)" }
                    token
                } else {
                    logger.d { "No token found in Keychain (status=$status)" }
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
            memScoped {
                val query = CFDictionaryCreateMutable(
                    kCFAllocatorDefault,
                    3.convert(),
                    kCFTypeDictionaryKeyCallBacks.ptr,
                    kCFTypeDictionaryValueCallBacks.ptr
                )

                CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
                val serviceStr = cfString(service)
                val accountStr = cfString(account)
                CFDictionaryAddValue(query, kSecAttrService, serviceStr)
                CFDictionaryAddValue(query, kSecAttrAccount, accountStr)

                val status = SecItemDelete(query)

                CFRelease(serviceStr)
                CFRelease(accountStr)
                CFRelease(query)

                if (status == errSecSuccess) {
                    logger.i { "Token cleared from Keychain" }
                } else {
                    logger.d { "No token to clear from Keychain" }
                }
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to clear token from Keychain" }
            throw SecureStorageException("Failed to clear token", e)
        }
    }

    override suspend fun hasToken(): Boolean {
        return try {
            memScoped {
                val query = CFDictionaryCreateMutable(
                    kCFAllocatorDefault,
                    4.convert(),
                    kCFTypeDictionaryKeyCallBacks.ptr,
                    kCFTypeDictionaryValueCallBacks.ptr
                )

                CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
                val serviceStr = cfString(service)
                val accountStr = cfString(account)
                CFDictionaryAddValue(query, kSecAttrService, serviceStr)
                CFDictionaryAddValue(query, kSecAttrAccount, accountStr)
                CFDictionaryAddValue(query, kSecReturnData, platform.CoreFoundation.kCFBooleanFalse)
                CFDictionaryAddValue(query, kSecMatchLimit, kSecMatchLimitOne)

                val result = alloc<COpaquePointerVar>()
                val status = SecItemCopyMatching(query, result.ptr)

                CFRelease(serviceStr)
                CFRelease(accountStr)
                CFRelease(query)

                status == errSecSuccess
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to check token existence in Keychain" }
            false
        }
    }

    private fun cfString(string: String): CFTypeRef {
        return CFStringCreateWithCString(
            kCFAllocatorDefault,
            string,
            platform.CoreFoundation.kCFStringEncodingUTF8
        )!!
    }
}
