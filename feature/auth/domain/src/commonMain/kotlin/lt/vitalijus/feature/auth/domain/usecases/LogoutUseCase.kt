package lt.vitalijus.feature.auth.domain.usecases

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.EmptyResult
import lt.vitalijus.feature.auth.domain.AuthRepository

/**
 * Use case for user logout.
 *
 * Note: The caller is responsible for clearing scans from ScanRepository.
 */
class LogoutUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): EmptyResult<DataError.Local> {
        return authRepository.logout()
    }
}
