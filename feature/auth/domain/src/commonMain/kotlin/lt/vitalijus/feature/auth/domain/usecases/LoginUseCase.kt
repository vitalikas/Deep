package lt.vitalijus.feature.auth.domain.usecases

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.auth.domain.AuthRepository
import lt.vitalijus.feature.auth.domain.LoginResult

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<LoginResult, DataError> {
        return authRepository.login(
            email = email.trim(),
            password = password
        )
    }
}
