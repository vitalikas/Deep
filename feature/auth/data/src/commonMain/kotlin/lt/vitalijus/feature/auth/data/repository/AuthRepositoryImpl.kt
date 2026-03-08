package lt.vitalijus.feature.auth.data.repository

import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.domain.logging.DeepLogger
import lt.vitalijus.core.domain.repository.ScanRepository
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.EmptyResult
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.core.security.TokenStorage
import lt.vitalijus.feature.auth.data.mappers.toDomain
import lt.vitalijus.feature.auth.data.mappers.toEntity
import lt.vitalijus.feature.auth.data.network.AuthApiService
import lt.vitalijus.feature.auth.domain.AuthRepository
import lt.vitalijus.feature.auth.domain.LoginResult

class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val userDao: UserDao,
    private val tokenStorage: TokenStorage,
    private val scanRepository: ScanRepository,
    private val logger: DeepLogger
) : AuthRepository {

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
                    // Save user data (without token - token goes to secure storage)
                    userDao.insertUser(user = loginResult.user.toEntity())
                    // Save token securely
                    when (val tokenResult = tokenStorage.saveToken(token = loginResult.token)) {
                        is Result.Success -> {
                            // Save scans
                            scanRepository.saveScans(scans = loginResult.scans)
                            logger.debug(message = "Login successful for user: ${loginResult.user.id}, saved ${loginResult.scans.size} scans")
                            Result.Success(loginResult)
                        }

                        is Result.Failure -> {
                            // Rollback: delete user since token storage failed
                            userDao.clearAllUsers()
                            logger.error(message = "Failed to save token, rolled back user: ${tokenResult.error}")
                            Result.Failure(DataError.Local.UNKNOWN)
                        }
                    }
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
            when (val clearResult = tokenStorage.clearToken()) {
                is Result.Success -> {
                    logger.debug(message = "Logout successful - all user data and token cleared")
                    Result.Success(Unit)
                }

                is Result.Failure -> {
                    logger.error(message = "Failed to clear token: ${clearResult.error}")
                    Result.Failure(DataError.Local.UNKNOWN)
                }
            }
        } catch (e: Exception) {
            logger.error(message = "Logout failed", throwable = e)
            Result.Failure(DataError.Local.UNKNOWN)
        }
    }
}
