package lt.vitalijus.feature.auth.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.domain.logging.DeepLogger
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.EmptyResult
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.auth.data.mappers.toDomain
import lt.vitalijus.feature.auth.data.mappers.toEntity
import lt.vitalijus.feature.auth.data.network.AuthApiService
import lt.vitalijus.feature.auth.data.network.TokenManager
import lt.vitalijus.feature.auth.domain.AuthRepository
import lt.vitalijus.feature.auth.domain.LoginResult
import lt.vitalijus.feature.auth.domain.User

class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val userDao: UserDao,
    private val tokenManager: TokenManager,
    private val logger: DeepLogger
) : AuthRepository {

    override val currentUser: Flow<User?> = userDao.getCurrentUser().map { it?.toDomain() }

    override val isAuthenticated: Flow<Boolean> = tokenManager.isAuthenticated

    override suspend fun login(
        email: String,
        password: String
    ): Result<LoginResult, DataError> {
        logger.debug(message = "Attempting login for email: $email")

        // Try network login first
        val networkResult = apiService.login(
            email = email,
            password = password
        )

        return when (networkResult) {
            is Result.Success -> {
                val loginResult = networkResult.data.toDomain()

                if (loginResult != null) {
                    // Save to local database
                    userDao.insertUser(user = loginResult.user.toEntity())
                    tokenManager.saveToken(
                        userId = loginResult.user.id,
                        token = loginResult.user.token,
                        validTill = loginResult.user.validTill
                    )
                    // Note: scans are in loginResult.scans, caller should save them to ScanRepository
                    logger.debug(message = "Login successful for user: ${loginResult.user.id}, loaded ${loginResult.scans.size} scans")
                    Result.Success(loginResult)
                } else {
                    logger.error(message = "Login response parsing failed")
                    Result.Failure(DataError.Remote.SERIALIZATION)
                }
            }

            is Result.Failure -> {
                logger.error(
                    message = "Login failed: ${networkResult.error}"
                )
                Result.Failure(networkResult.error)
            }
        }
    }

    override suspend fun logout(): EmptyResult<DataError.Local> {
        return try {
            // Clear all user data including token for security
            userDao.clearAllUsers()
            logger.debug(message = "Logout successful - all user data cleared")
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.error(message = "Logout failed", throwable = e)
            Result.Failure(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getCurrentToken(): String? {
        return tokenManager.getToken()
    }

    override suspend fun isTokenValid(): Boolean {
        val currentUser = userDao.getCurrentUser().first()
        return tokenManager.isTokenValid(validTill = currentUser?.validTill)
    }
}
