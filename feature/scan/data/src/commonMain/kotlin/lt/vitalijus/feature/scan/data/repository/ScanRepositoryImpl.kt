package lt.vitalijus.feature.scan.data.repository

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.auth.domain.AuthRepository
import lt.vitalijus.feature.scan.data.network.ScanApiService
import lt.vitalijus.core.database.dao.ScanDao
import lt.vitalijus.core.database.entity.ScanEntity
import lt.vitalijus.feature.scan.domain.model.BathymetryData
import lt.vitalijus.feature.scan.domain.repository.ScanRepository
import lt.vitalijus.feature.scan.domain.model.Scan

class ScanRepositoryImpl(
    private val apiService: ScanApiService,
    private val authRepository: AuthRepository,
    private val scanDao: ScanDao
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
        scanDao.insertScans(scans.map { it.toEntity() })
    }

    override suspend fun clearScans() {
        scanDao.clearAllScans()
    }

    override suspend fun getBathymetryData(scanId: Long): Result<BathymetryData, DataError> {
        val token = authRepository.getCurrentToken()
            ?: return Result.Failure(DataError.Remote.UNAUTHORIZED)
        return apiService.getBathymetry(scanId, token)
    }
}

private fun ScanEntity.toDomain(): Scan {
    return Scan(
        id = id,
        lat = lat,
        lon = lon,
        name = name,
        date = date,
        scanPoints = scanPoints,
        mode = mode
    )
}

private fun Scan.toEntity(): ScanEntity {
    return ScanEntity(
        id = id,
        lat = lat,
        lon = lon,
        name = name,
        date = date,
        scanPoints = scanPoints,
        mode = mode
    )
}
