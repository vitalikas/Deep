package lt.vitalijus.deep.di

import lt.vitalijus.deep.app.AppMiddleware
import lt.vitalijus.deep.app.AppViewModel
import lt.vitalijus.feature.auth.domain.usecases.IsAuthenticatedUseCase
import lt.vitalijus.feature.auth.domain.usecases.LogoutUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for App-level dependencies.
 */
val appModule = module {
    factory {
        AppMiddleware(
            isAuthenticatedUseCase = get<IsAuthenticatedUseCase>(),
            logoutUseCase = get<LogoutUseCase>()
        )
    }

    viewModel {
        AppViewModel(middleware = get())
    }
}
