package lt.vitalijus.deep.di

import lt.vitalijus.deep.app.AppMiddleware
import lt.vitalijus.deep.app.AppViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for App-level dependencies.
 */
val appModule = module {
    factory { AppMiddleware(isAuthenticatedUseCase = get()) }

    viewModel {
        AppViewModel(middleware = get())
    }
}
