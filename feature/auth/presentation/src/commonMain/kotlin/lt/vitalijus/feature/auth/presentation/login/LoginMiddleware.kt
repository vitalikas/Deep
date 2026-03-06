package lt.vitalijus.feature.auth.presentation.login

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.onFailure
import lt.vitalijus.core.domain.util.onSuccess
import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.core.presentation.util.UiText
import lt.vitalijus.core.presentation.util.toStringResource
import lt.vitalijus.feature.auth.domain.usecases.LoginUseCase

/**
 * Middleware for handling login side effects.
 *
 * Uses Compose Multiplatform resources for localization via core:presentation extension.
 *
 * @param loginUseCase Use case for authentication operations
 */
class LoginMiddleware(
    private val loginUseCase: LoginUseCase
) : Middleware<LoginIntent, LoginState, LoginEffect> {

    override suspend fun handle(
        intent: LoginIntent,
        state: LoginState,
        dispatchIntent: suspend (LoginIntent) -> Unit,
        emitEffect: suspend (LoginEffect) -> Unit
    ) {
        when (intent) {
            is LoginIntent.OnLoginClick -> handleLogin(
                email = state.email,
                password = state.password,
                dispatchIntent = dispatchIntent,
                emitEffect = emitEffect
            )

            else -> {
                /* Other intents don't need side effects */
            }
        }
    }

    private suspend fun handleLogin(
        email: String,
        password: String,
        dispatchIntent: suspend (LoginIntent) -> Unit,
        emitEffect: suspend (LoginEffect) -> Unit
    ) {
        loginUseCase(
            email = email,
            password = password
        )
            .onSuccess {
                emitEffect(LoginEffect.Navigate)
            }
            .onFailure { error: DataError ->
                val errorMessage = UiText.Resource(error.toStringResource())
                emitEffect(LoginEffect.ShowToast(message = errorMessage))
                dispatchIntent(LoginIntent.OnLoginError(message = errorMessage))
            }
    }
}
