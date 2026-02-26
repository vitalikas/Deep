package lt.vitalijus.feature.auth.presentation.di

import lt.vitalijus.feature.auth.presentation.login.LoginMiddleware
import lt.vitalijus.feature.auth.presentation.login.LoginViewModel
import lt.vitalijus.feature.scan.domain.repository.ScanRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authPresentationModule = module {
    factory { LoginMiddleware(loginUseCase = get(), scanRepository = get<ScanRepository>()) }

    viewModel {
        LoginViewModel(middleware = get())
    }
}
