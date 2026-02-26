package lt.vitalijus.feature.scan.domain.usecase

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.scan.domain.repository.ScanRepository
import lt.vitalijus.feature.scan.domain.model.Scan

/**
 * Use case for getting user's scan list.
 */
class GetScansUseCase(
    private val scanRepository: ScanRepository
) {

    suspend operator fun invoke(): Result<List<Scan>, DataError> {
        return scanRepository.getScans()
    }
}
