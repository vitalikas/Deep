package lt.vitalijus.feature.auth.presentation.login

sealed interface LoginEvent {
    data class OnEmailChange(val email: String) : LoginEvent
    data class OnPasswordChange(val password: String) : LoginEvent
    data object OnTogglePasswordVisibility : LoginEvent
    data class OnLoginClick(val onSuccess: () -> Unit) : LoginEvent
    data object OnErrorDismiss : LoginEvent
}
