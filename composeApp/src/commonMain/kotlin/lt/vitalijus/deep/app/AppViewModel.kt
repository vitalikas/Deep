package lt.vitalijus.deep.app

import lt.vitalijus.core.presentation.mvi.MviViewModel

/**
 * App-level ViewModel for managing global app state.
 *
 * Features:
 * - Handles authentication state
 * - Manages app-level navigation decisions
 * - Reducer handles pure state transformations
 * - Middleware handles side effects (auth checks)
 *
 * Auth check happens once via initialIntent (survives config changes).
 */
class AppViewModel(
    middleware: AppMiddleware
) : MviViewModel<AppIntent, AppState, Nothing>(
    initialState = AppState.Initializing,
    reducer = createAppReducer(),
    middleware = middleware,
    initialIntent = AppIntent.CheckAuth  // Auto-dispatch once on ViewModel creation
)
