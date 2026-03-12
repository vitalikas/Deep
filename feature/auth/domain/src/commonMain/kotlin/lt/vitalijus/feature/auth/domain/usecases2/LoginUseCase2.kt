package lt.vitalijus.feature.auth.domain.usecases2

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.auth.domain.LoginResult

class LoginUseCase2(
    private val authRepository2: FakeAuthRepository2
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<LoginResult, DataError> {
        val trimmedEmail = email.trim()

        if (trimmedEmail.isBlank() || password.isBlank()) {
            return Result.Failure(DataError.Validation.EMPTY_FIELDS)
        }

        if (!isEmailValid(trimmedEmail)) {
            return Result.Failure(DataError.Validation.INVALID_EMAIL)
        }

        return authRepository2.login(
            email = trimmedEmail,
            password = password
        )
    }

    companion object {
        private val EMAIL_REGEX = Regex(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        )

        fun isEmailValid(email: String): Boolean = EMAIL_REGEX.matches(email)
    }
}
