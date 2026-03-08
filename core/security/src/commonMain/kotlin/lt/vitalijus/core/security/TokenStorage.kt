package lt.vitalijus.core.security

import lt.vitalijus.core.domain.util.Result

/**
 * Pure storage interface for authentication tokens.
 * Knows nothing about users, sessions, or business logic.
 *
 * Platform implementations:
 * - Android: Encrypted DataStore + Keystore
 * - iOS: Keychain
 */
interface TokenStorage {
    /**
     * Check if token exists (no decryption).
     * @return Success(true) if token exists, Success(false) if not, or Failure(StorageError)
     */
    suspend fun hasToken(): Result<Boolean, StorageError>

    /**
     * Save token to secure storage. Overwrites existing.
     * @return Success(Unit) or Failure(StorageError)
     */
    suspend fun saveToken(token: String): Result<Unit, StorageError>

    /**
     * Get token from secure storage.
     * @return Success(token) or Failure(StorageError.NotFound) if no token
     */
    suspend fun getToken(): Result<String, StorageError>

    /**
     * Clear token from secure storage.
     * @return Success(Unit) or Failure(StorageError)
     */
    suspend fun clearToken(): Result<Unit, StorageError>
}
