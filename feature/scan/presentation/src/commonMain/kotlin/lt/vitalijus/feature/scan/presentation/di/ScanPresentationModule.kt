package lt.vitalijus.feature.scan.presentation.di

import lt.vitalijus.feature.scan.presentation.scandetail.ScanDetailMiddleware
import lt.vitalijus.feature.scan.presentation.scandetail.ScanDetailViewModel
import lt.vitalijus.feature.scan.presentation.scans.ScanListMiddleware
import lt.vitalijus.feature.scan.presentation.scans.ScanListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val scanPresentationModule = module {
    // Scan List
    factory {
        ScanListMiddleware(
            getScansUseCase = get(),
            logoutUseCase = get(),
            clearAllCacheUseCase = get()
        )
    }

    viewModel {
        ScanListViewModel(
            middleware = get()
        )
    }

    // Scan Detail
    factory { ScanDetailMiddleware(getBathymetryUseCase = get()) }

    viewModel { (scanId: Long, scanName: String) ->
        ScanDetailViewModel(
            middleware = get()
        )
    }
}
