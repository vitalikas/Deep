package lt.vitalijus.feature.scan.domain

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result

/**
 * Use case for fetching bathymetry data for a scan.
 */
class GetBathymetryUseCase(
    private val repository: ScanRepository
) {

    suspend operator fun invoke(scanId: Long): Result<BathymetryData, DataError> {
        return repository.getBathymetryData(scanId)
    }
}
