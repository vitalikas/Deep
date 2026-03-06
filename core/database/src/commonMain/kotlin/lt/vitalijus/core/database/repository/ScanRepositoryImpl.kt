package lt.vitalijus.core.database.repository

import lt.vitalijus.core.database.dao.BathymetryDao
import lt.vitalijus.core.database.dao.ScanDao
import lt.vitalijus.core.database.mappers.toBathymetryData
import lt.vitalijus.core.database.mappers.toDomain
import lt.vitalijus.core.database.mappers.toEntities
import lt.vitalijus.core.database.mappers.toEntity
import lt.vitalijus.core.domain.model.BathymetryData
import lt.vitalijus.core.domain.model.Scan
import lt.vitalijus.core.domain.repository.ScanRepository
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result

/**
 * Local repository implementation for scan data operations.
 * This provides an offline-first data layer independent of any feature module.
 */
class ScanRepositoryImpl(
    private val scanDao: ScanDao,
    private val bathymetryDao: BathymetryDao
) : ScanRepository {

    override suspend fun getScans(): Result<List<Scan>, DataError> {
        return try {
            val scans = scanDao.getAllScansSync().map { it.toDomain() }
            Result.Success(scans)
        } catch (e: Exception) {
            val error = when {
                e.message?.contains("disk full", ignoreCase = true) == true ||
                        e.message?.contains("database is full", ignoreCase = true) == true ->
                    DataError.Local.DISK_FULL

                else -> DataError.Local.UNKNOWN
            }
            Result.Failure(error)
        }
    }

    override suspend fun saveScans(scans: List<Scan>) {
        scanDao.insertScans(scans = scans.toEntities())
    }

    override suspend fun clearScans() {
        scanDao.clearAllScans()
    }

    override suspend fun clearAllCache() {
        scanDao.clearAllScans()
        bathymetryDao.clearAll()
    }

    override suspend fun getBathymetryData(scanId: Long): Result<BathymetryData?, DataError> {
        return try {
            val cached = bathymetryDao.getByScanId(scanId = scanId)
            Result.Success(cached?.jsonData?.toBathymetryData())
        } catch (e: Exception) {
            val error = when {
                e.message?.contains("disk full", ignoreCase = true) == true ||
                        e.message?.contains("database is full", ignoreCase = true) == true ->
                    DataError.Local.DISK_FULL

                else -> DataError.Local.UNKNOWN
            }
            Result.Failure(error)
        }
    }

    override suspend fun saveBathymetryData(
        scanId: Long,
        data: BathymetryData
    ) {
        bathymetryDao.insert(bathymetry = data.toEntity(scanId = scanId))
    }
}
