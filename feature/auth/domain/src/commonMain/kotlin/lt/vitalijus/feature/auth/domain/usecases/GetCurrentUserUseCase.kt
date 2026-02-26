package lt.vitalijus.feature.auth.domain.usecases

import kotlinx.coroutines.flow.Flow
import lt.vitalijus.feature.auth.domain.AuthRepository
import lt.vitalijus.feature.auth.domain.User

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): Flow<User?> {
        return authRepository.currentUser
    }
}
