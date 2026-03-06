package lt.vitalijus.core.domain.repository

import lt.vitalijus.core.domain.model.BathymetryData
import lt.vitalijus.core.domain.model.Scan
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result

/**
 * Repository for scan-related data operations.
 */
interface ScanRepository {
    /**
     * Get user's scan list from local cache.
     *
     * @return Result containing list of scans or error
     */
    suspend fun getScans(): Result<List<Scan>, DataError>

    /**
     * Save scans to local cache (e.g., after successful login).
     *
     * @param scans List of scans to cache
     */
    suspend fun saveScans(scans: List<Scan>)

    /**
     * Clear cached scans (e.g., on logout).
     */
    suspend fun clearScans()

    /**
     * Clear all cached data including scans and bathymetry (e.g., on logout).
     */
    suspend fun clearAllCache()

    /**
     * Get bathymetry GeoJSON data for a specific scan from cache.
     * Returns null if not cached.
     *
     * @param scanId The scan ID
     * @return Result containing bathymetry data or error
     */
    suspend fun getBathymetryData(scanId: Long): Result<BathymetryData?, DataError>

    /**
     * Save bathymetry data to cache.
     *
     * @param scanId The scan ID
     * @param data The bathymetry data to cache
     */
    suspend fun saveBathymetryData(
        scanId: Long,
        data: BathymetryData
    )
}
