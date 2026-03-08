package lt.vitalijus.feature.auth.data.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.database.entity.UserEntity
import lt.vitalijus.core.security.TokenStorage

/**
 * Authentication state tracking.
 * Combines user presence (DB) and token presence (SecureStorage).
 */
interface AuthStateManager {
    /**
     * Reactive auth state. Emits true if both user and token exist.
     */
    val isAuthenticated: Flow<Boolean>

    /**
     * Current snapshot - synchronous check.
     */
    suspend fun checkAuthenticated(): Boolean
}

class AuthStateManagerImpl(
    private val tokenStorage: TokenStorage,
    private val userDao: UserDao
) : AuthStateManager {

    override val isAuthenticated: Flow<Boolean> = combine(
        tokenStorage.tokenFlow,
        userDao.getCurrentUser()
    ) { token: String?, user: UserEntity? ->
        token != null && user != null
    }

    override suspend fun checkAuthenticated(): Boolean {
        val hasToken = tokenStorage.hasToken()
        val hasUser = userDao.getCurrentUserSync() != null
        return hasToken && hasUser
    }
}
