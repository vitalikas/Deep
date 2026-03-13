package lt.vitalijus.feature.scan.presentation.scandetail

import lt.vitalijus.core.domain.model.BathymetryData
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.core.domain.util.onFailure
import lt.vitalijus.core.domain.util.onSuccess
import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.feature.scan.domain.usecase.GetBathymetryUseCase

/**
 * Middleware for handling scan detail side effects.
 */
class ScanDetailMiddleware(
    private val getBathymetryUseCase: Lazy<GetBathymetryUseCase>
) : Middleware<ScanDetailIntent, ScanDetailState, ScanDetailEffect> {

    override suspend fun handle(
        intent: ScanDetailIntent,
        state: ScanDetailState,
        dispatchIntent: suspend (ScanDetailIntent) -> Unit,
        emitEffect: suspend (ScanDetailEffect) -> Unit
    ) {
        when (intent) {
            is ScanDetailIntent.LoadScan -> {
                loadBathymetry(
                    scanId = intent.scanId,
                    dispatchIntent = dispatchIntent,
                    emitEffect = emitEffect
                )
            }

            is ScanDetailIntent.OnBackClick -> {
                emitEffect(ScanDetailEffect.NavigateBack)
            }

            else -> {} // OnBathymetryLoaded and OnError are handled by reducer
        }
    }

    private suspend fun loadBathymetry(
        scanId: Long,
        dispatchIntent: suspend (ScanDetailIntent) -> Unit,
        emitEffect: suspend (ScanDetailEffect) -> Unit
    ) {
        val result: Result<BathymetryData, DataError> = getBathymetryUseCase.value(scanId)
        result
            .onSuccess { bathymetryData ->
                dispatchIntent(
                    ScanDetailIntent.OnBathymetryLoaded(
                        polygons = bathymetryData.polygons,
                        bbox = bathymetryData.bbox
                    )
                )
            }
            .onFailure { error: DataError ->
                val message = "Failed to load bathymetry: ${error::class.simpleName}"
                dispatchIntent(ScanDetailIntent.OnError(message))
                emitEffect(ScanDetailEffect.ShowToast(message))
            }
    }
}
