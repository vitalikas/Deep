package lt.vitalijus.deep.app

import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.feature.auth.data.auth.AuthStateManager
import lt.vitalijus.feature.auth.domain.usecases.LogoutUseCase

/**
 * Middleware for handling app-level side effects.
 */
class AppMiddleware(
    private val authStateManager: AuthStateManager,
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
                val isAuthenticated = authStateManager.checkAuthenticated()
                dispatchIntent(AppIntent.AuthChecked(isAuthenticated = isAuthenticated))
            }

            is AppIntent.Logout -> {
                logoutUseCase()
            }

            else -> {} // AuthChecked handled by reducer only
        }
    }
}
