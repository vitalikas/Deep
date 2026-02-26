package lt.vitalijus.feature.auth.presentation.login

import lt.vitalijus.core.presentation.mvi.MviViewModel

/**
 * Login ViewModel with MVI+Redux pattern.
 *
 * Features:
 * - Reducer handles pure state transformations
 * - Middleware handles side effects (API calls) - injected via constructor
 * - All state updates go through Reducer only
 */
class LoginViewModel(
    middleware: LoginMiddleware
) : MviViewModel<LoginIntent, LoginState, LoginEffect>(
    initialState = LoginState(),
    reducer = createLoginReducer(),
    middleware = middleware
)
