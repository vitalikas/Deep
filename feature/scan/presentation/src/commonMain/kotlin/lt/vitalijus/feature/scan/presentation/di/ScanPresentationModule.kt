package lt.vitalijus.feature.scan.presentation.di

import lt.vitalijus.feature.scan.presentation.scans.ScanListMiddleware
import lt.vitalijus.feature.scan.presentation.scans.ScanListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val scanPresentationModule = module {
    factory { ScanListMiddleware(getScansUseCase = get()) }

    viewModel {
        ScanListViewModel(
            middleware = get()
        )
    }
}
