package lt.vitalijus.feature.auth.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lt.vitalijus.core.database.dao.UserDao
import kotlin.time.Clock
import kotlin.time.Instant

interface TokenManager {
    val authToken: Flow<String?>
    val isAuthenticated: Flow<Boolean>

    suspend fun saveToken(userId: Long, token: String, validTill: String?)
    suspend fun getToken(): String?
    suspend fun clearToken(userId: Long)
    suspend fun isTokenValid(validTill: String?): Boolean
}

class TokenManagerImpl(
    private val userDao: UserDao
) : TokenManager {

    override val authToken: Flow<String?> = userDao.getCurrentUser().map { it?.token }

    override val isAuthenticated: Flow<Boolean> = userDao.getCurrentUser().map { it != null }

    override suspend fun saveToken(userId: Long, token: String, validTill: String?) {
        userDao.updateToken(userId, token)
    }

    override suspend fun getToken(): String? {
        return userDao.getCurrentToken()
    }

    override suspend fun clearToken(userId: Long) {
        userDao.logout(userId)
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
