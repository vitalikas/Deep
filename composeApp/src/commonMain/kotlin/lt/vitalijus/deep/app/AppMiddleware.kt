package lt.vitalijus.deep.app

import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.feature.auth.domain.usecases.IsAuthenticatedUseCase
import lt.vitalijus.feature.auth.domain.usecases.LogoutUseCase

/**
 * Middleware for handling app-level side effects.
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
                val isAuthenticated = isAuthenticatedUseCase()
                dispatchIntent(AppIntent.AuthChecked(isAuthenticated = isAuthenticated))
            }

            is AppIntent.Logout -> {
                logoutUseCase()
            }

            else -> {} // AuthChecked handled by reducer only
        }
    }
}
