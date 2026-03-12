package lt.vitalijus.feature.auth.data.repository2.fakes

import lt.vitalijus.core.domain.model.BathymetryData
import lt.vitalijus.core.domain.model.Scan
import lt.vitalijus.core.domain.repository.ScanRepository
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result

class FakeScanRepository2 : ScanRepository {

    var savedScans: List<Scan> = emptyList()
        private set

    override suspend fun getScans(): Result<List<Scan>, DataError> {
        return Result.Success(savedScans)
    }

    override suspend fun saveScans(scans: List<Scan>) {
        savedScans = scans
    }

    override suspend fun clearScans() {
        savedScans = emptyList()
    }

    override suspend fun clearAllCache() {
        savedScans = emptyList()
    }

    override suspend fun getBathymetryData(scanId: Long): Result<BathymetryData?, DataError> {
        return Result.Success(null)
    }

    override suspend fun saveBathymetryData(scanId: Long, data: BathymetryData) {}
}
