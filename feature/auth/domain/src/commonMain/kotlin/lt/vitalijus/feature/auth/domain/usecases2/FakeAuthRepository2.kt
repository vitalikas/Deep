package lt.vitalijus.feature.auth.domain.usecases2

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.EmptyResult
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.auth.domain.AuthRepository
import lt.vitalijus.feature.auth.domain.LoginResult
import lt.vitalijus.feature.auth.domain.User

class FakeAuthRepository2(
    private val result: Result<LoginResult, DataError> = Result.Success(
        LoginResult(
            user = User(
                id = 1,
                email = "test@test.com"
            ),
            token = "fake-token",
            scans = emptyList()
        )
    )
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Result<LoginResult, DataError> {
        return result
    }

    override suspend fun logout(): EmptyResult<DataError.Local> {
        return Result.Success(Unit)
    }
}
