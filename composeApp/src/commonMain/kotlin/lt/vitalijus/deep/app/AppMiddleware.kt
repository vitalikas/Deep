package lt.vitalijus.deep.app

import kotlinx.coroutines.flow.collectLatest
import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.feature.auth.domain.usecases.IsAuthenticatedUseCase

/**
 * Middleware for handling app-level side effects.
 *
 * Observes authentication state changes automatically and updates AppState.
 */
class AppMiddleware(
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase
) : Middleware<AppIntent, AppState, Nothing> {

    override suspend fun handle(
        intent: AppIntent,
        state: AppState,
        dispatchIntent: suspend (AppIntent) -> Unit,
        emitEffect: suspend (Nothing) -> Unit
    ) {
        when (intent) {
            is AppIntent.CheckAuth -> {
                isAuthenticatedUseCase().collectLatest { isAuthenticated ->
                    dispatchIntent(AppIntent.AuthChecked(isAuthenticated = isAuthenticated))
                }
            }

            else -> {} // AuthChecked, Logout handled by reducer only
        }
    }
}
