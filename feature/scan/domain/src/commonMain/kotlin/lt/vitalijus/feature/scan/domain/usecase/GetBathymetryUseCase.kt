package lt.vitalijus.feature.scan.domain.usecase

import lt.vitalijus.core.domain.model.BathymetryData
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result

/**
 * Use case for fetching bathymetry data for a scan.
 * Implementations should provide offline-first approach: checks cache first, falls back to API.
 */
fun interface GetBathymetryUseCase {
    suspend operator fun invoke(scanId: Long): Result<BathymetryData, DataError>
}
