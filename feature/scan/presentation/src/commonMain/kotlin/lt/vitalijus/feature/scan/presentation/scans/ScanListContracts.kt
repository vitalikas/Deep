package lt.vitalijus.feature.scan.presentation.scans

import lt.vitalijus.core.presentation.mvi.UiEffect
import lt.vitalijus.core.presentation.mvi.UiIntent
import lt.vitalijus.core.presentation.mvi.UiState

/**
 * Scan list UI state with MVI pattern.
 */
data class ScanListState(
    val scans: List<ScanUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedScanId: Long? = null,
    val portraitScrollPosition: Int = 0,
    val twoPaneScrollPosition: Int = 0
) : UiState

/**
 * Scan list intents (actions).
 */
sealed class ScanListIntent : UiIntent {
    data object LoadScans : ScanListIntent()
    data class OnScansLoaded(val scans: List<ScanUiModel>) : ScanListIntent()
    data class OnScanClick(val scanId: Long) : ScanListIntent()
    data object OnRefresh : ScanListIntent()
    data class OnError(val message: String) : ScanListIntent()
    data object OnLogoutClick : ScanListIntent()
    data object OnLoggedOut : ScanListIntent()
    data class OnSelectScan(val scanId: Long) : ScanListIntent()
}

/**
 * Scan list effects (one-time events).
 */
sealed class ScanListEffect : UiEffect {
    data class NavigateToScanDetail(val scanId: Long) : ScanListEffect()
    data class ShowToast(val message: String) : ScanListEffect()
    data object LogoutRequested : ScanListEffect()
}

/**
 * UI model for scan item.
 */
data class ScanUiModel(
    val id: Long,
    val name: String,
    val date: String,
    val location: String,
    val scanPoints: Int
)
