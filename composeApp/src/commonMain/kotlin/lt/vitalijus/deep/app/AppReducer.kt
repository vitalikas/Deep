package lt.vitalijus.deep.app

import lt.vitalijus.core.presentation.mvi.Reducer
import lt.vitalijus.core.presentation.mvi.reducer

/**
 * Pure reducer for App-level state transformations.
 */
internal fun createAppReducer(): Reducer<AppState, AppIntent> = reducer {
    on<AppIntent.CheckAuth> { state, _ ->
        state.copy(isLoading = true)
    }

    on<AppIntent.AuthChecked> { state, intent ->
        state.copy(
            isLoading = false,
            isAuthenticated = intent.isAuthenticated
        )
    }

    on<AppIntent.Logout> { state, _ ->
        state.copy(isAuthenticated = false)
    }
}
