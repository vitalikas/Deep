package lt.vitalijus.feature.auth.presentation.login

import lt.vitalijus.core.presentation.mvi.UiIntent
import lt.vitalijus.core.presentation.util.UiText

/**
 * Login intents (actions) representing user intentions.
 */
sealed class LoginIntent : UiIntent {
    data class OnEmailChange(val email: String) : LoginIntent()
    data class OnPasswordChange(val password: String) : LoginIntent()
    data object OnTogglePasswordVisibility : LoginIntent()
    data object OnLoginClick : LoginIntent()
    data class OnLoginError(val message: UiText) : LoginIntent()
    data object OnErrorDismiss : LoginIntent()
}
