package lt.vitalijus.feature.auth.data.adapter

import kotlinx.coroutines.flow.first
import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.domain.auth.TokenProvider
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.core.security.TokenStorage
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Adapter that implements TokenProvider from core-domain
 * using TokenStorage directly.
 */
class TokenProviderAdapter(
    private val tokenStorage: TokenStorage,
    private val userDao: UserDao
) : TokenProvider {

    override suspend fun getToken(): String? {
        return when (val result = tokenStorage.getToken()) {
            is Result.Success -> result.data
            is Result.Failure -> null
        }
    }

    override suspend fun isTokenValid(): Boolean {
        val currentUser = userDao.getCurrentUser().first()
        val validTill = currentUser?.validTill

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
