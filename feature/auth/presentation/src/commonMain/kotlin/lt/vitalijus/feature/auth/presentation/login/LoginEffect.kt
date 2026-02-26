package lt.vitalijus.feature.auth.presentation.login

import lt.vitalijus.core.presentation.mvi.UiEffect
import lt.vitalijus.core.presentation.util.UiText

/**
 * Login effects - one-time side effects like navigation, toasts.
 */
sealed class LoginEffect : UiEffect {
    data object Navigate : LoginEffect()
    data class ShowToast(val message: UiText) : LoginEffect()
}
