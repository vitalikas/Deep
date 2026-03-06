package lt.vitalijus.deep.app

import kotlinx.coroutines.flow.collectLatest
import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.feature.auth.domain.usecases.IsAuthenticatedUseCase
import lt.vitalijus.feature.auth.domain.usecases.LogoutUseCase

/**
 * Middleware for handling app-level side effects.
 *
 * Observes authentication state changes automatically and updates AppState.
 */
class AppMiddleware(
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase,
    private val logoutUseCase: LogoutUseCase
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

            is AppIntent.Logout -> {
                // Perform actual logout
                logoutUseCase()
            }

            else -> {} // AuthChecked handled by reducer only
        }
    }
}
