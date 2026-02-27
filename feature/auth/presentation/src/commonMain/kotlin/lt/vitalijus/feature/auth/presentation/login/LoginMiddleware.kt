package lt.vitalijus.feature.auth.presentation.login

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.onFailure
import lt.vitalijus.core.domain.util.onSuccess
import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.core.presentation.util.UiText
import lt.vitalijus.feature.auth.domain.usecases.LoginUseCase
import lt.vitalijus.feature.auth.presentation.mappers.toScans
import lt.vitalijus.feature.scan.domain.repository.ScanRepository

/**
 * Middleware for handling login side effects.
 *
 * @param loginUseCase Use case for authentication operations
 * @param scanRepository Repository for saving scans after successful login
 */
class LoginMiddleware(
    private val loginUseCase: LoginUseCase,
    private val scanRepository: ScanRepository
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
            .onSuccess { loginResult ->
                // Save scans to repository - this is the source of truth
                scanRepository.saveScans(loginResult.scans.toScans())
                emitEffect(LoginEffect.Navigate)
            }
            .onFailure { error: DataError ->
                val errorMessage = mapError(error = error)
                emitEffect(LoginEffect.ShowToast(message = errorMessage))
                dispatchIntent(LoginIntent.OnLoginError(message = errorMessage))
            }
    }

    private fun mapError(error: DataError): UiText {
        return when (error) {
            is DataError.Remote -> when (error) {
                DataError.Remote.UNAUTHORIZED ->
                    UiText.DynamicString("Invalid email or password")

                DataError.Remote.BAD_REQUEST ->
                    UiText.DynamicString("Invalid request")

                DataError.Remote.REQUEST_TIMEOUT ->
                    UiText.DynamicString("Request timed out. Please try again.")

                DataError.Remote.NO_INTERNET ->
                    UiText.DynamicString("No internet connection")

                DataError.Remote.SERVER_ERROR ->
                    UiText.DynamicString("Server error. Please try again later.")

                else ->
                    UiText.DynamicString("An error occurred: ${error.name}")
            }

            is DataError.Local ->
                UiText.DynamicString("A local error occurred: ${error.name}")

            is DataError.Validation -> when (error) {
                DataError.Validation.INVALID_EMAIL ->
                    UiText.DynamicString("Invalid email format")

                DataError.Validation.EMPTY_FIELDS ->
                    UiText.DynamicString("Email and password cannot be empty")

                DataError.Validation.INVALID_PASSWORD ->
                    UiText.DynamicString("Invalid password")
            }
        }
    }
}
