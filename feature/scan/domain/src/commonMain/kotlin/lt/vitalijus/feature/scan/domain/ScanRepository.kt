package lt.vitalijus.feature.scan.domain

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result

/**
 * Repository for scan-related data operations.
 */
interface ScanRepository {
    /**
     * Get bathymetry GeoJSON data for a specific scan.
     *
     * @param scanId The scan ID
     * @return Result containing bathymetry data or error
     */
    suspend fun getBathymetryData(scanId: Long): Result<BathymetryData, DataError>
}
