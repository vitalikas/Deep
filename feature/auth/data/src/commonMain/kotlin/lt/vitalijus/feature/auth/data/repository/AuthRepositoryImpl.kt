package lt.vitalijus.feature.auth.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import lt.vitalijus.core.domain.logging.DeepLogger
import lt.vitalijus.core.domain.model.Scan
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.EmptyResult
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.auth.data.local.UserDao
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

    // Scans from last login, stored in memory
    private var cachedScans: List<Scan> = emptyList()

    override val scans: Flow<List<Scan>> = kotlinx.coroutines.flow.flow {
        emit(cachedScans)
    }

    override suspend fun login(email: String, password: String): Result<LoginResult, DataError> {
        logger.debug(message = "Attempting login for email: $email")

        // Try network login first
        val networkResult = apiService.login(email = email, password = password)

        return when (networkResult) {
            is Result.Success -> {
                val loginResult = networkResult.data.toDomain()
                val scanDtos = networkResult.data.scans

                if (loginResult != null) {
                    // Save to local database
                    userDao.insertUser(loginResult.user.toEntity())
                    tokenManager.saveToken(
                        userId = loginResult.user.id,
                        token = loginResult.user.token,
                        validTill = loginResult.user.validTill
                    )
                    // Cache scans from login response
                    cachedScans = scanDtos?.map { it.toDomain() } ?: emptyList()
                    logger.debug(message = "Login successful for user: ${loginResult.user.id}, loaded ${cachedScans.size} scans")
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
            val currentUser = userDao.getCurrentUserSync()
            currentUser?.let {
                tokenManager.clearToken(it.id)
                userDao.logout(it.id)
            }
            cachedScans = emptyList()
            logger.debug(message = "Logout successful")
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
        return tokenManager.isTokenValid(currentUser?.validTill)
    }

    override suspend fun getScans(): Result<List<Scan>, DataError> {
        return Result.Success(cachedScans)
    }
}
