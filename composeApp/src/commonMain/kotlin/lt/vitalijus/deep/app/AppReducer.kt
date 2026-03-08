package lt.vitalijus.deep.app

import lt.vitalijus.core.presentation.mvi.Reducer
import lt.vitalijus.core.presentation.mvi.reducer

/**
 * Pure reducer for App-level state transformations.
 */
internal fun createAppReducer(): Reducer<AppState, AppIntent> = reducer {
    on<AppIntent.CheckAuth> { _, _ ->
        AppState.Initializing
    }

    on<AppIntent.AuthChecked> { _, intent ->
        if (intent.isAuthenticated) {
            AppState.Authenticated
        } else {
            AppState.Unauthenticated
        }
    }

    on<AppIntent.Logout> { _, _ ->
        AppState.Unauthenticated
    }
}
