package lt.vitalijus.core.security

/**
 * Secure storage for sensitive data like authentication tokens.
 *
 * Platform implementations:
 * - Android: Encrypted DataStore (Keystore-backed)
 * - iOS: Keychain
 */
interface SecureStorage {

    /**
     * Saves authentication token securely.
     *
     * @param token The token to save
     * @throws SecurityException if encryption fails
     */
    suspend fun saveToken(token: String)

    /**
     * Retrieves the stored token.
     *
     * @return The token or null if not found
     * @throws SecurityException if decryption fails
     */
    suspend fun getToken(): String?

    /**
     * Removes the stored token.
     */
    suspend fun clearToken()

    /**
     * Checks if a token exists in secure storage.
     */
    suspend fun hasToken(): Boolean
}

/**
 * Exception thrown when secure storage operations fail.
 */
class SecureStorageException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
