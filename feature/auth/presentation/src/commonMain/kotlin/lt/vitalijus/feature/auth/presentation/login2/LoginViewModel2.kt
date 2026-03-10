package lt.vitalijus.feature.auth.presentation.login2

import lt.vitalijus.core.presentation.mvi.MviViewModel

class LoginViewModel2(
    middleware: LoginMiddleware2
) : MviViewModel<LoginIntent2, LoginState2, LoginEffect2>(
    initialState = LoginState2(),
    initialIntent = null,
    reducer = createLoginReducer2(),
    middleware = middleware
)
