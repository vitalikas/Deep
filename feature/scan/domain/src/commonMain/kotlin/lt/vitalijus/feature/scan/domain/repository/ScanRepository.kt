package lt.vitalijus.feature.scan.domain.repository

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.scan.domain.model.BathymetryData
import lt.vitalijus.feature.scan.domain.model.Scan

/**
 * Repository for scan-related data operations.
 */
interface ScanRepository {
    /**
     * Get user's scan list.
     *
     * @return Result containing list of scans or error
     */
    suspend fun getScans(): Result<List<Scan>, DataError>

    /**
     * Save scans (e.g., after successful login).
     *
     * @param scans List of scans to cache
     */
    suspend fun saveScans(scans: List<Scan>)

    /**
     * Clear cached scans (e.g., on logout).
     */
    suspend fun clearScans()

    /**
     * Get bathymetry GeoJSON data for a specific scan.
     *
     * @param scanId The scan ID
     * @return Result containing bathymetry data or error
     */
    suspend fun getBathymetryData(scanId: Long): Result<BathymetryData, DataError>
}