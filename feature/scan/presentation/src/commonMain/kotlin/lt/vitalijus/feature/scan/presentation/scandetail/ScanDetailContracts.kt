package lt.vitalijus.feature.scan.presentation.scandetail

import androidx.compose.runtime.Immutable
import lt.vitalijus.core.presentation.mvi.UiEffect
import lt.vitalijus.core.presentation.mvi.UiIntent
import lt.vitalijus.core.presentation.mvi.UiState
import lt.vitalijus.feature.scan.domain.model.Polygon

/**
 * Scan detail UI state with MVI pattern.
 */
@Immutable
data class ScanDetailState(
    val scanId: Long = 0,
    val scanName: String = "",
    val polygons: List<Polygon> = emptyList(),
    val bbox: List<Double> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : UiState

/**
 * Scan detail intents (actions).
 */
sealed class ScanDetailIntent : UiIntent {
    data class LoadScan(
        val scanId: Long,
        val scanName: String
    ) : ScanDetailIntent()

    @Immutable
    data class OnBathymetryLoaded(
        val polygons: List<Polygon>,
        val bbox: List<Double>
    ) : ScanDetailIntent()

    data class OnError(val message: String) : ScanDetailIntent()
    data object OnBackClick : ScanDetailIntent()
}

/**
 * Scan detail effects (one-time events).
 */
sealed class ScanDetailEffect : UiEffect {
    data object NavigateBack : ScanDetailEffect()
    data class ShowToast(val message: String) : ScanDetailEffect()
}
