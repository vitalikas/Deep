package lt.vitalijus.feature.auth.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.security.SecureStorage
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * TokenManager that securely stores tokens using platform-specific secure storage.
 *
 * - Android: Encrypted DataStore with Keystore
 * - iOS: Keychain
 */
interface TokenManager {
    val authToken: Flow<String?>

    suspend fun saveToken(token: String, validTill: String?)
    suspend fun getToken(): String?
    suspend fun clearToken()
    suspend fun isTokenValid(validTill: String?): Boolean
    suspend fun isAuthenticated(): Boolean
}

class TokenManagerImpl(
    private val secureStorage: SecureStorage,
    private val userDao: UserDao
) : TokenManager {

    // In-memory cache for reactive updates
    private val _authToken = MutableStateFlow<String?>(null)
    override val authToken: Flow<String?> = _authToken.asStateFlow()

    override suspend fun isAuthenticated(): Boolean {
        val hasUser = userDao.getCurrentUserSync() != null
        val hasToken = secureStorage.hasToken()
        return hasUser && hasToken
    }

    override suspend fun saveToken(token: String, validTill: String?) {
        secureStorage.saveToken(token)
        _authToken.update { token }
    }

    override suspend fun getToken(): String? {
        val token = secureStorage.getToken()
        _authToken.update { token }
        return token
    }

    override suspend fun clearToken() {
        secureStorage.clearToken()
        _authToken.update { null }
    }

    override suspend fun isTokenValid(validTill: String?): Boolean {
        validTill ?: return true // If no expiry, assume valid

        return try {
            val expiry = Instant.parse(validTill)
            val now = Clock.System.now()
            now < expiry
        } catch (_: Exception) {
            true // If parsing fails, assume valid
        }
    }
}
