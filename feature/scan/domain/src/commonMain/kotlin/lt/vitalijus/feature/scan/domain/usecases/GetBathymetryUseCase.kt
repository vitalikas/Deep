package lt.vitalijus.feature.scan.domain.usecases

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.scan.domain.Bathymetry
import lt.vitalijus.feature.scan.domain.ScanRepository

/**
 * Use case for fetching bathymetry data for a scan.
 */
class GetBathymetryUseCase(
    private val repository: ScanRepository
) {
    suspend operator fun invoke(scanId: Long): Result<Bathymetry, DataError> {
        return repository.getBathymetry(scanId)
    }
}
