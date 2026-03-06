package lt.vitalijus.feature.scan.domain.usecase

import lt.vitalijus.core.domain.model.Scan
import lt.vitalijus.core.domain.repository.ScanRepository
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result

/**
 * Use case for getting user's scan list from local cache.
 */
class GetScansUseCase(
    private val scanRepository: ScanRepository
) {

    suspend operator fun invoke(): Result<List<Scan>, DataError> {
        return scanRepository.getScans()
    }
}
