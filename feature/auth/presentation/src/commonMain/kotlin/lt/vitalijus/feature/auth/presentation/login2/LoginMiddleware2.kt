package lt.vitalijus.feature.auth.presentation.login2

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.core.presentation.mvi.Middleware

class LoginMiddleware2(
    private val authApi: FakeAuthApi
) : Middleware<LoginIntent2, LoginState2, LoginEffect2> {

    override suspend fun handle(
        intent: LoginIntent2,
        state: LoginState2,
        dispatchIntent: suspend (LoginIntent2) -> Unit,
        emitEffect: suspend (LoginEffect2) -> Unit
    ) {
        when (intent) {
            LoginIntent2.OnLoginClicked -> {
                // async API call
                val result = authApi.login(
                    email = state.email,
                    password = state.password
                )

                when (result) {
                    is Result.Success -> {
                        emitEffect(LoginEffect2.Navigate)
                    }

                    is Result.Failure -> {
                        val errorMessage = when (result.error) {
                            DataError.Remote.UNAUTHORIZED -> "Invalid credentials"
                            else -> "Login failed"
                        }
                        emitEffect(LoginEffect2.ShowError(message = errorMessage))
                    }
                }
            }

            else -> Unit
        }
    }
}
