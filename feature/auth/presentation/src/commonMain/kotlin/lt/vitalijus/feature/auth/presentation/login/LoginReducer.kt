package lt.vitalijus.feature.auth.presentation.login

import lt.vitalijus.core.presentation.mvi.Reducer
import lt.vitalijus.core.presentation.mvi.reducer
import lt.vitalijus.feature.auth.domain.usecases.LoginUseCase

/**
 * Pure reducer for Login feature - no side effects, just state transformations.
 */
internal fun createLoginReducer(): Reducer<LoginState, LoginIntent> = reducer {
    on<LoginIntent.OnEmailChange> { state, intent ->
        state.copy(
            email = intent.email,
            errorMessage = null,
            isEmailValid = intent.email.isBlank() || LoginUseCase.isEmailValid(intent.email)
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
