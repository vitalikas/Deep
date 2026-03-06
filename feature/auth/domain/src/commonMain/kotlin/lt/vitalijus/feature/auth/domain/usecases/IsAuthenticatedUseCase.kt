package lt.vitalijus.feature.auth.domain.usecases

import lt.vitalijus.feature.auth.domain.AuthRepository

class IsAuthenticatedUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Boolean {
        return authRepository.isAuthenticated()
    }
}
