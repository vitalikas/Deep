package lt.vitalijus.deep.app

import lt.vitalijus.core.presentation.mvi.UiIntent
import lt.vitalijus.core.presentation.mvi.UiState

/**
 * App-level UI state.
 */
data class AppState(
    val isLoading: Boolean = true,
    val isAuthenticated: Boolean = false
) : UiState

/**
 * App-level intents (actions).
 */
sealed interface AppIntent : UiIntent {
    data object CheckAuth : AppIntent
    data class AuthChecked(val isAuthenticated: Boolean) : AppIntent
    data object Logout : AppIntent
}
