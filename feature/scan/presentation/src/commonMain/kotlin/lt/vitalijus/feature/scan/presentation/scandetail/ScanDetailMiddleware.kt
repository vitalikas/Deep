package lt.vitalijus.feature.scan.presentation.scandetail

import lt.vitalijus.core.domain.util.onFailure
import lt.vitalijus.core.domain.util.onSuccess
import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.feature.scan.domain.usecases.GetBathymetryUseCase

/**
 * Middleware for handling scan detail side effects.
 */
class ScanDetailMiddleware(
    private val getBathymetryUseCase: GetBathymetryUseCase
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
            else -> { } // OnBathymetryLoaded and OnError are handled by reducer
        }
    }

    private suspend fun loadBathymetry(
        scanId: Long,
        dispatchIntent: suspend (ScanDetailIntent) -> Unit,
        emitEffect: suspend (ScanDetailEffect) -> Unit
    ) {
        getBathymetryUseCase(scanId)
            .onSuccess { bathymetry ->
                dispatchIntent(
                    ScanDetailIntent.OnBathymetryLoaded(
                        features = bathymetry.features,
                        bbox = bathymetry.bbox
                    )
                )
            }
            .onFailure { error ->
                val message = "Failed to load bathymetry: ${error::class.simpleName}"
                dispatchIntent(ScanDetailIntent.OnError(message))
                emitEffect(ScanDetailEffect.ShowToast(message))
            }
    }
}
