package lt.vitalijus.feature.auth.presentation.login2

import lt.vitalijus.core.presentation.mvi.UiIntent

sealed interface LoginIntent2 : UiIntent {
    data class OnEmailChanged(val email: String) : LoginIntent2
    data class OnPasswordChanged(val password: String) : LoginIntent2
    data object OnLoginClicked : LoginIntent2
}
