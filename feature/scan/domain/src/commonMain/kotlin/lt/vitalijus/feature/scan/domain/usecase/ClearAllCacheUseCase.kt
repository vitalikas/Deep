package lt.vitalijus.feature.scan.domain.usecase

import lt.vitalijus.core.domain.repository.ScanRepository
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.EmptyResult
import lt.vitalijus.core.domain.util.Result

/**
 * Use case to clear all cached scan data including scans and bathymetry.
 * Should be called on logout to ensure data privacy.
 */
class ClearAllCacheUseCase(
    private val scanRepository: ScanRepository
) {

    suspend operator fun invoke(): EmptyResult<DataError.Local> {
        return try {
            scanRepository.clearAllCache()
            Result.Success(Unit)
        } catch (_: Exception) {
            Result.Failure(DataError.Local.UNKNOWN)
        }
    }
}
