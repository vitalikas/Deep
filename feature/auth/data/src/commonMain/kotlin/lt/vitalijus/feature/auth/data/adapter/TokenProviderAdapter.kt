package lt.vitalijus.feature.auth.data.adapter

import kotlinx.coroutines.flow.first
import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.domain.auth.TokenProvider
import lt.vitalijus.feature.auth.data.network.TokenManager

/**
 * Adapter that implements TokenProvider from core-domain
 * by delegating to the TokenManager.
 */
class TokenProviderAdapter(
    private val tokenManager: TokenManager,
    private val userDao: UserDao
) : TokenProvider {

    override suspend fun getToken(): String? {
        return tokenManager.getToken()
    }

    override suspend fun isTokenValid(): Boolean {
        val currentUser = userDao.getCurrentUser().first()
        return tokenManager.isTokenValid(validTill = currentUser?.validTill)
    }
}
