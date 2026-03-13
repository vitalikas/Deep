package lt.vitalijus.deep.di

import lt.vitalijus.core.security.TokenStorage
import lt.vitalijus.deep.app.AppMiddleware
import lt.vitalijus.deep.app.AppViewModel
import lt.vitalijus.feature.auth.domain.usecases.LogoutUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for App-level dependencies.
 */
val appModule = module {
    factory {
        AppMiddleware(
            tokenStorage = lazy { get<TokenStorage>() },
            logoutUseCase = lazy { get<LogoutUseCase>() }
        )
    }

    viewModel {
        AppViewModel(middleware = get())
    }
}
