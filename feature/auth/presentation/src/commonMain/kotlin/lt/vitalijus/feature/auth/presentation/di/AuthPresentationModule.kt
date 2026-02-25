package lt.vitalijus.feature.auth.presentation.di

import lt.vitalijus.feature.auth.presentation.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authPresentationModule = module {
    viewModel {
        LoginViewModel(
            loginUseCase = get()
        )
    }
}
