package lt.vitalijus.feature.auth.presentation.login2

import lt.vitalijus.core.presentation.mvi.UiState

data class LoginState2(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
) : UiState
