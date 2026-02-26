package lt.vitalijus.feature.auth.domain.usecases

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.core.domain.model.Scan
import lt.vitalijus.feature.auth.domain.AuthRepository

/**
 * Use case for getting user's scan list.
 */
class GetScansUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<List<Scan>, DataError> {
        return authRepository.getScans()
    }
}
