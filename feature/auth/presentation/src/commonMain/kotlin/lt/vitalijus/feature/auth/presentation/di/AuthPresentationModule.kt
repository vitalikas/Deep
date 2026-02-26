package lt.vitalijus.feature.auth.presentation.di

import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.feature.auth.presentation.login.LoginEffect
import lt.vitalijus.feature.auth.presentation.login.LoginIntent
import lt.vitalijus.feature.auth.presentation.login.LoginMiddleware
import lt.vitalijus.feature.auth.presentation.login.LoginState
import lt.vitalijus.feature.auth.presentation.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authPresentationModule = module {
    // Middleware registered with explicit generic type
    factory<Middleware<LoginIntent, LoginState, LoginEffect>> {
        LoginMiddleware(loginUseCase = get())
    }

    viewModel {
        LoginViewModel(
            middleware = get()
        )
    }
}
