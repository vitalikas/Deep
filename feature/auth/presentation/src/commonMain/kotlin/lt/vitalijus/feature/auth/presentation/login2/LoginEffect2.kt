package lt.vitalijus.feature.auth.presentation.login2

import lt.vitalijus.core.presentation.mvi.UiEffect

sealed interface LoginEffect2 : UiEffect {
    data class ShowError(val message: String) : LoginEffect2
    data object Navigate : LoginEffect2
}
