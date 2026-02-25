package lt.vitalijus.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.onFailure
import lt.vitalijus.core.domain.util.onSuccess
import lt.vitalijus.core.presentation.util.UiText
import lt.vitalijus.feature.auth.domain.usecases.LoginUseCase

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> {
                updateState { copy(email = event.email, errorMessage = null) }
            }

            is LoginEvent.OnPasswordChange -> {
                updateState { copy(password = event.password, errorMessage = null) }
            }

            is LoginEvent.OnTogglePasswordVisibility -> {
                updateState { copy(isPasswordVisible = !isPasswordVisible) }
            }

            is LoginEvent.OnLoginClick -> {
                performLogin(onSuccess = event.onSuccess)
            }

            is LoginEvent.OnErrorDismiss -> {
                updateState { copy(errorMessage = null) }
            }
        }
    }

    /**
     * Updates the state using the partial state update pattern (Redux-style).
     * This allows for atomic state updates and better state consistency.
     */
    private fun updateState(update: LoginState.() -> LoginState) {
        _state.update { it.update() }
    }

    private fun performLogin(onSuccess: () -> Unit) {
        val currentState = _state.value

        // Validation
        val email = currentState.email.trim()
        val password = currentState.password

        if (email.isBlank()) {
            updateState {
                copy(errorMessage = UiText.DynamicString("Email cannot be empty"))
            }
            return
        }

        if (password.isBlank()) {
            updateState {
                copy(errorMessage = UiText.DynamicString("Password cannot be empty"))
            }
            return
        }

        // Set loading state
        updateState {
            copy(
                isLoading = true,
                errorMessage = null,
                isLoginSuccessful = false
            )
        }

        viewModelScope.launch {
            loginUseCase(email = email, password = password)
                .onSuccess { loginResult ->
                    updateState {
                        copy(
                            isLoading = false,
                            isLoginSuccessful = true
                        )
                    }
                    onSuccess()
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        is DataError.Remote -> when (error) {
                            DataError.Remote.UNAUTHORIZED ->
                                UiText.DynamicString("Invalid email or password")

                            DataError.Remote.BAD_REQUEST ->
                                UiText.DynamicString("Invalid request")

                            DataError.Remote.REQUEST_TIMEOUT ->
                                UiText.DynamicString("Request timed out. Please try again.")

                            DataError.Remote.NO_INTERNET ->
                                UiText.DynamicString("No internet connection. Please check your connection.")

                            DataError.Remote.SERVER_ERROR ->
                                UiText.DynamicString("Server error. Please try again later.")

                            else ->
                                UiText.DynamicString("An error occurred: ${error.name}")
                        }

                        is DataError.Local ->
                            UiText.DynamicString("A local error occurred: ${error.name}")
                    }
                    updateState {
                        copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                }
        }
    }
}
