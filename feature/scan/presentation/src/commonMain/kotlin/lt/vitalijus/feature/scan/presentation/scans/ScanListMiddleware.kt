package lt.vitalijus.feature.scan.presentation.scans

import lt.vitalijus.core.domain.util.onFailure
import lt.vitalijus.core.domain.util.onSuccess
import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.feature.scan.domain.usecase.GetScansUseCase

/**
 * Middleware for handling scan list side effects.
 */
class ScanListMiddleware(
    private val getScansUseCase: GetScansUseCase
) : Middleware<ScanListIntent, ScanListState, ScanListEffect> {

    override suspend fun handle(
        intent: ScanListIntent,
        state: ScanListState,
        dispatchIntent: suspend (ScanListIntent) -> Unit,
        emitEffect: suspend (ScanListEffect) -> Unit
    ) {
        when (intent) {
            is ScanListIntent.LoadScans, is ScanListIntent.OnRefresh -> {
                loadScans(
                    dispatchIntent = dispatchIntent,
                    emitEffect = emitEffect
                )
            }

            is ScanListIntent.OnScanClick -> {
                emitEffect(ScanListEffect.NavigateToScanDetail(scanId = intent.scanId))
            }

            else -> {} // OnScansLoaded and OnError are handled by reducer only
        }
    }

    private suspend fun loadScans(
        dispatchIntent: suspend (ScanListIntent) -> Unit,
        emitEffect: suspend (ScanListEffect) -> Unit
    ) {
        getScansUseCase()
            .onSuccess { scans ->
                dispatchIntent(ScanListIntent.OnScansLoaded(scans = scans.toUiModels()))
            }
            .onFailure { error ->
                dispatchIntent(ScanListIntent.OnError(message = "Failed to load scans: $error"))
                emitEffect(ScanListEffect.ShowToast(message = "Failed to load scans"))
            }
    }
}
