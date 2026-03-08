package lt.vitalijus.deep.app

import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.core.security.TokenStorage
import lt.vitalijus.feature.auth.domain.usecases.LogoutUseCase

/**
 * Middleware for handling app-level side effects.
 * Auth state determined solely by token presence in secure storage.
 */
class AppMiddleware(
    private val tokenStorage: TokenStorage,
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
                when (val hasTokenResult = tokenStorage.hasToken()) {
                    is Result.Success -> {
                        dispatchIntent(AppIntent.AuthChecked(isAuthenticated = hasTokenResult.data))
                    }

                    is Result.Failure -> {
                        // Log error but treat as unauthenticated for safety
                        dispatchIntent(AppIntent.AuthChecked(isAuthenticated = false))
                    }
                }
            }

            is AppIntent.Logout -> {
                logoutUseCase()
            }

            else -> {} // AuthChecked handled by reducer only
        }
    }
}
