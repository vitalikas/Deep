package lt.vitalijus.deep.app

import lt.vitalijus.core.presentation.mvi.UiIntent
import lt.vitalijus.core.presentation.mvi.UiState

/**
 * App-level UI state represented as sealed interface.
 * Makes invalid states unrepresentable.
 */
sealed interface AppState : UiState {
    data object Initializing : AppState
    data object Authenticated : AppState
    data object Unauthenticated : AppState
}

/**
 * App-level intents (actions).
 */
sealed interface AppIntent : UiIntent {
    data object CheckAuth : AppIntent
    data class AuthChecked(val isAuthenticated: Boolean) : AppIntent
    data object Logout : AppIntent
}
