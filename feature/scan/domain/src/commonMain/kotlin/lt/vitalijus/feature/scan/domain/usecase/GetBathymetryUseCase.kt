package lt.vitalijus.feature.scan.domain.usecase

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.scan.domain.model.BathymetryData
import lt.vitalijus.feature.scan.domain.repository.ScanRepository

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