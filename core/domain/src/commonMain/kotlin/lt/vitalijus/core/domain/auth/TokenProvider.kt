package lt.vitalijus.core.domain.auth

/**
 * Interface for providing authentication token.
 */
interface TokenProvider {
    /**
     * Get the current authentication token.
     * @return The token string or null if not authenticated.
     */
    suspend fun getToken(): String?

    /**
     * Check if the current token is valid (not expired).
     * @return True if token is valid, false otherwise.
     */
    suspend fun isTokenValid(): Boolean
}
