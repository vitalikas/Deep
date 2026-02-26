package lt.vitalijus.feature.auth.domain.usecases

import kotlinx.coroutines.flow.Flow
import lt.vitalijus.feature.auth.domain.AuthRepository

class IsAuthenticatedUseCase(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): Flow<Boolean> {
        return authRepository.isAuthenticated
    }
}
