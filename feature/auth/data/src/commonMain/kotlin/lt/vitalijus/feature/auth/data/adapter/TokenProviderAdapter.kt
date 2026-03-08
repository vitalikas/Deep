package lt.vitalijus.feature.auth.data.adapter

import kotlinx.coroutines.flow.first
import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.domain.auth.TokenProvider
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
        return tokenStorage.getToken()
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
