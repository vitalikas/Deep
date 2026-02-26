package lt.vitalijus.feature.scan.data.repository

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.scan.data.network.ScanApiService
import lt.vitalijus.feature.scan.domain.Bathymetry
import lt.vitalijus.feature.scan.domain.ScanRepository

class ScanRepositoryImpl(
    private val apiService: ScanApiService
) : ScanRepository {

    override suspend fun getBathymetry(scanId: Long): Result<Bathymetry, DataError> {
        return apiService.getBathymetry(scanId)
    }
}
