package lt.vitalijus.feature.scan.data.repository

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.scan.data.network.ScanApiService
import lt.vitalijus.feature.scan.domain.ScanRepository
import lt.vitalijus.feature.scan.domain.BathymetryData

class ScanRepositoryImpl(
    private val apiService: ScanApiService
) : ScanRepository {

    override suspend fun getBathymetryData(scanId: Long): Result<BathymetryData, DataError> {
        return apiService.getBathymetry(scanId)
    }
}
