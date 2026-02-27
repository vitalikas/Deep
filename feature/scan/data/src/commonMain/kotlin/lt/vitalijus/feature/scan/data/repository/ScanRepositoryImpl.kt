package lt.vitalijus.feature.scan.data.repository

import lt.vitalijus.core.database.dao.BathymetryDao
import lt.vitalijus.core.database.dao.ScanDao
import lt.vitalijus.core.database.entity.BathymetryEntity
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.auth.domain.AuthRepository
import lt.vitalijus.feature.scan.data.mappers.toBathymetryData
import lt.vitalijus.feature.scan.data.mappers.toDomain
import lt.vitalijus.feature.scan.data.mappers.toEntities
import lt.vitalijus.feature.scan.data.mappers.toJson
import lt.vitalijus.feature.scan.data.network.ScanApiService
import lt.vitalijus.feature.scan.domain.model.BathymetryData
import lt.vitalijus.feature.scan.domain.model.Scan
import lt.vitalijus.feature.scan.domain.repository.ScanRepository

/**
 * Repository implementation with offline-first approach:
 * 1. Try to get from local cache
 * 2. If not cached, fetch from API
 * 3. Save successful API response to cache
 */
class ScanRepositoryImpl(
    private val apiService: ScanApiService,
    private val authRepository: AuthRepository,
    private val scanDao: ScanDao,
    private val bathymetryDao: BathymetryDao
) : ScanRepository {

    override suspend fun getScans(): Result<List<Scan>, DataError> {
        return try {
            val scans = scanDao.getAllScansSync().map { it.toDomain() }
            Result.Success(scans)
        } catch (e: Exception) {
            Result.Failure(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun saveScans(scans: List<Scan>) {
        scanDao.insertScans(scans = scans.toEntities())
    }

    override suspend fun clearScans() {
        scanDao.clearAllScans()
    }

    override suspend fun getBathymetryData(scanId: Long): Result<BathymetryData, DataError> {
        // 1. Try to get from cache first (offline-first)
        val cached = bathymetryDao.getByScanId(scanId = scanId)
        if (cached != null) {
            return try {
                val bathymetry = cached.jsonData.toBathymetryData()
                Result.Success(bathymetry)
            } catch (e: Exception) {
                // Cache corrupted, delete and fetch from API
                bathymetryDao.deleteByScanId(scanId)
                fetchFromApi(scanId)
            }
        }

        // 2. Not cached, fetch from API
        return fetchFromApi(scanId)
    }

    private suspend fun fetchFromApi(scanId: Long): Result<BathymetryData, DataError> {
        val token = authRepository.getCurrentToken()
            ?: return Result.Failure(DataError.Remote.UNAUTHORIZED)

        return when (val result = apiService.getBathymetry(scanId, token)) {
            is Result.Success -> {
                val bathymetryData = result.data
                // Save to cache for offline use
                try {
                    bathymetryDao.insert(
                        BathymetryEntity(
                            scanId = scanId,
                            jsonData = bathymetryData.toJson()
                        )
                    )
                } catch (e: Exception) {
                    // Log but don't fail — API data is still valid
                }
                Result.Success(bathymetryData)
            }

            is Result.Failure -> {
                Result.Failure(result.error)
            }
        }
    }
}
