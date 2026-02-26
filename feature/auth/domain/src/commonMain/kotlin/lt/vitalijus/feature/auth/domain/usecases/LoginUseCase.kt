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
        val trimmedEmail = email.trim()
        
        // Validation
        if (trimmedEmail.isBlank() || password.isBlank()) {
            return Result.Failure(DataError.Validation.EMPTY_FIELDS)
        }
        
        if (!isEmailValid(trimmedEmail)) {
            return Result.Failure(DataError.Validation.INVALID_EMAIL)
        }
        
        return authRepository.login(
            email = trimmedEmail,
            password = password
        )
    }
    
    companion object {
        // RFC 5322 compliant email regex
        private val EMAIL_REGEX = Regex(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        )
        
        fun isEmailValid(email: String): Boolean {
            return EMAIL_REGEX.matches(email)
        }
    }
}
