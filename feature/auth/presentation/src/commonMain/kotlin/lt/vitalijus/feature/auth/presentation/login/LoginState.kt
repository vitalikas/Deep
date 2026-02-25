package lt.vitalijus.feature.auth.presentation.login

import lt.vitalijus.core.presentation.util.UiText

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val isLoginSuccessful: Boolean = false
)
