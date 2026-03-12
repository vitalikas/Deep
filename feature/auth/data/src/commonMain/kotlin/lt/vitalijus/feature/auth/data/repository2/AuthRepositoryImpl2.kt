package lt.vitalijus.feature.auth.data.repository2

import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.domain.logging.DeepLogger
import lt.vitalijus.core.domain.repository.ScanRepository
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.EmptyResult
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.core.security.TokenStorage
import lt.vitalijus.feature.auth.data.mappers.toDomain
import lt.vitalijus.feature.auth.data.mappers.toEntity
import lt.vitalijus.feature.auth.data.network2.AuthApiService2
import lt.vitalijus.feature.auth.domain.AuthRepository
import lt.vitalijus.feature.auth.domain.LoginResult

class AuthRepositoryImpl2(
    private val apiService: AuthApiService2,
    private val userDao: UserDao,
    private val tokenStorage: TokenStorage,
    private val scanRepository: ScanRepository,
    private val logger: DeepLogger
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Result<LoginResult, DataError> {
        val networkResult = apiService.login(
            email = email,
            password = password
        )

        return when (networkResult) {
            is Result.Failure -> {
                Result.Failure(error = networkResult.error)
            }

            is Result.Success -> {
                val loginResult = networkResult.data.toDomain()

                if (loginResult != null) {
                    try {
                        userDao.insertUser(user = loginResult.user.toEntity())
                    } catch (_: Exception) {
                        return Result.Failure(DataError.Local.UNKNOWN)
                    }
                    val tokenResult = tokenStorage.saveToken(token = loginResult.token)
                    when (tokenResult) {
                        is Result.Success -> {
                            scanRepository.saveScans(scans = loginResult.scans)
                            Result.Success(loginResult)
                        }

                        is Result.Failure -> {
                            try {
                                userDao.clearAllUsers() // rollback!
                            } catch (_: Exception) {
                                // rollback failed, still return error
                            }
                            Result.Failure(DataError.Local.UNKNOWN)
                        }
                    }
                } else {
                    Result.Failure(DataError.Remote.SERVER_ERROR)
                }
            }
        }
    }

    override suspend fun logout(): EmptyResult<DataError.Local> {
        TODO()
    }
}
