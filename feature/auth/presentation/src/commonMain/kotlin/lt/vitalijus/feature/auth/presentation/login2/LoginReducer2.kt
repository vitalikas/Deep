package lt.vitalijus.feature.auth.presentation.login2

import lt.vitalijus.core.presentation.mvi.Reducer
import lt.vitalijus.core.presentation.mvi.reducer

fun createLoginReducer2(): Reducer<LoginState2, LoginIntent2> = reducer {
    on<LoginIntent2.OnEmailChanged> { state2, intent2 ->
        state2.copy(email = intent2.email)
    }

    on<LoginIntent2.OnPasswordChanged> { state2, intent2 ->
        state2.copy(password = intent2.password)
    }

    on<LoginIntent2.OnLoginClicked> { state2, intent2 ->
        state2.copy(isLoading = true)
    }
}
