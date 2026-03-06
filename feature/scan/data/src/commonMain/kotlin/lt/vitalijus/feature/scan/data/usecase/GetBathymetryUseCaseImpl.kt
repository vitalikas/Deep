package lt.vitalijus.feature.scan.data.usecase

import lt.vitalijus.core.domain.auth.TokenProvider
import lt.vitalijus.core.domain.model.BathymetryData
import lt.vitalijus.core.domain.repository.ScanRepository
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.scan.data.network.ScanApiService
import lt.vitalijus.feature.scan.domain.usecase.GetBathymetryUseCase

/**
 * Implementation of GetBathymetryUseCase with offline-first approach.
 */
class GetBathymetryUseCaseImpl(
    private val scanRepository: ScanRepository,
    private val apiService: ScanApiService,
    private val tokenProvider: TokenProvider
) : GetBathymetryUseCase {

    override suspend operator fun invoke(scanId: Long): Result<BathymetryData, DataError> {
        // 1. Try to get from cache first
        val cachedResult = scanRepository.getBathymetryData(scanId = scanId)
        val cachedData = (cachedResult as? Result.Success)?.data
        if (cachedData != null) {
            return Result.Success(cachedData)
        }

        // 2. Not cached, fetch from API - need token
        val token = tokenProvider.getToken()
            ?: return Result.Failure(DataError.Remote.UNAUTHORIZED)
        val bathymetryDataResult = apiService.getBathymetry(
            scanId = scanId,
            token = token
        )
        return when (bathymetryDataResult) {
            is Result.Success -> {
                // Save to cache for offline use
                try {
                    scanRepository.saveBathymetryData(
                        scanId = scanId, data = bathymetryDataResult.data
                    )
                } catch (_: Exception) {
                    // Log but don't fail - API data is still valid
                }
                Result.Success(bathymetryDataResult.data)
            }

            is Result.Failure -> {
                Result.Failure(bathymetryDataResult.error)
            }
        }
    }
}
