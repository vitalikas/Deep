package lt.vitalijus.feature.scan.presentation.scans

import lt.vitalijus.core.domain.util.onFailure
import lt.vitalijus.core.domain.util.onSuccess
import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.feature.auth.domain.usecases.GetScansUseCase

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
            is ScanListIntent.LoadScans, is ScanListIntent.OnRefresh -> loadScans(dispatchIntent, emitEffect)
            is ScanListIntent.OnScanClick -> {
                emitEffect(ScanListEffect.NavigateToScanDetail(intent.scanId))
            }
            else -> { } // OnScansLoaded and OnError are handled by reducer only
        }
    }

    private suspend fun loadScans(
        dispatchIntent: suspend (ScanListIntent) -> Unit,
        emitEffect: suspend (ScanListEffect) -> Unit
    ) {
        getScansUseCase()
            .onSuccess { scans ->
                val uiModels = scans.map { scan ->
                    ScanUiModel(
                        id = scan.id,
                        name = scan.name ?: "Scan #${scan.id}",
                        date = formatDate(scan.date),
                        location = "${scan.lat}, ${scan.lon}",
                        scanPoints = scan.scanPoints
                    )
                }
                dispatchIntent(ScanListIntent.OnScansLoaded(uiModels))
            }
            .onFailure { error ->
                dispatchIntent(ScanListIntent.OnError("Failed to load scans: ${error}"))
                emitEffect(ScanListEffect.ShowToast("Failed to load scans"))
            }
    }

    private fun formatDate(dateString: String?): String {
        return dateString ?: "Unknown date"
    }
}
