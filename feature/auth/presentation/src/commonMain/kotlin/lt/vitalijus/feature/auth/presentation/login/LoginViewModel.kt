package lt.vitalijus.feature.auth.presentation.login

import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.core.presentation.mvi.MviViewModel
import lt.vitalijus.core.presentation.mvi.Reducer
import lt.vitalijus.core.presentation.mvi.reducer
import lt.vitalijus.feature.auth.domain.usecases.LoginUseCase

/**
 * Login ViewModel with MVI+Redux pattern.
 *
 * Features:
 * - Reducer handles pure state transformations
 * - Middleware handles side effects (API calls) - injected via constructor
 * - All state updates go through Reducer only
 */
class LoginViewModel(
    middleware: Middleware<LoginIntent, LoginState, LoginEffect>
) : MviViewModel<LoginIntent, LoginState, LoginEffect>(
    initialState = LoginState(),
    reducer = createReducer(),
    middleware = middleware
) {

    companion object {
        /**
         * Pure reducer - no side effects, just state transformations.
         */
        private fun createReducer(): Reducer<LoginState, LoginIntent> = reducer {
            on<LoginIntent.OnEmailChange> { state, intent ->
                state.copy(
                    email = intent.email,
                    errorMessage = null,
                    isEmailValid = intent.email.isBlank() || LoginUseCase.isEmailValid(email = intent.email)
                )
            }

            on<LoginIntent.OnPasswordChange> { state, intent ->
                state.copy(
                    password = intent.password,
                    errorMessage = null
                )
            }

            on<LoginIntent.OnTogglePasswordVisibility> { state, _ ->
                state.copy(isPasswordVisible = !state.isPasswordVisible)
            }

            on<LoginIntent.OnLoginClick> { state, _ ->
                // Set loading, middleware handles the API call
                state.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            on<LoginIntent.OnLoginError> { state, intent ->
                state.copy(
                    isLoading = false,
                    errorMessage = intent.message
                )
            }

            on<LoginIntent.OnErrorDismiss> { state, _ ->
                state.copy(errorMessage = null)
            }
        }
    }
}
