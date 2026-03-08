package lt.vitalijus.core.security

import kotlinx.coroutines.flow.StateFlow

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
     * Reactive stream of current token. Emits null if no token stored.
     */
    val tokenFlow: StateFlow<String?>

    /**
     * Synchronously check if token exists (no decryption).
     */
    suspend fun hasToken(): Boolean

    /**
     * Save token to secure storage. Overwrites existing.
     */
    suspend fun saveToken(token: String)

    /**
     * Get token from secure storage.
     * Also updates in-memory cache (tokenFlow).
     */
    suspend fun getToken(): String?

    /**
     * Clear token from secure storage.
     */
    suspend fun clearToken()
}
