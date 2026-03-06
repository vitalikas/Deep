package lt.vitalijus.feature.auth.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.domain.logging.DeepLogger
import lt.vitalijus.core.domain.repository.ScanRepository
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
    private val scanRepository: ScanRepository,
    private val logger: DeepLogger
) : AuthRepository {

    override val currentUser: Flow<User?> = userDao.getCurrentUser().map { it?.toDomain() }

    override val isAuthenticated: Flow<Boolean> = tokenManager.isAuthenticated

    override suspend fun login(
        email: String,
        password: String
    ): Result<LoginResult, DataError> {
        logger.debug(message = "Attempting login for email: $email")

        val networkResult = apiService.login(
            email = email,
            password = password
        )

        return when (networkResult) {
            is Result.Success -> {
                val loginResult = networkResult.data.toDomain()

                if (loginResult != null) {
                    // Save user data
                    userDao.insertUser(user = loginResult.user.toEntity())
                    // Save token
                    tokenManager.saveToken(
                        userId = loginResult.user.id,
                        token = loginResult.user.token,
                        validTill = loginResult.user.validTill
                    )
                    // Save scans
                    scanRepository.saveScans(scans = loginResult.scans)
                    logger.debug(message = "Login successful for user: ${loginResult.user.id}, saved ${loginResult.scans.size} scans")
                    Result.Success(loginResult)
                } else {
                    logger.error(message = "Login response missing required fields")
                    Result.Failure(DataError.Remote.SERVER_ERROR)
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
            userDao.clearAllUsers()
            logger.debug(message = "Logout successful - all user data cleared")
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.error(message = "Logout failed", throwable = e)
            Result.Failure(DataError.Local.UNKNOWN)
        }
    }
}
