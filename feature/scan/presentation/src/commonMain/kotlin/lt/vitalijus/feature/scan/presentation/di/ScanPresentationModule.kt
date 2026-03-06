package lt.vitalijus.feature.scan.presentation.di

import lt.vitalijus.feature.scan.domain.usecase.ClearAllCacheUseCase
import lt.vitalijus.feature.scan.domain.usecase.GetBathymetryUseCase
import lt.vitalijus.feature.scan.domain.usecase.GetScansUseCase
import lt.vitalijus.feature.scan.presentation.scandetail.ScanDetailMiddleware
import lt.vitalijus.feature.scan.presentation.scandetail.ScanDetailViewModel
import lt.vitalijus.feature.scan.presentation.scans.ScanListMiddleware
import lt.vitalijus.feature.scan.presentation.scans.ScanListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val scanPresentationModule = module {
    // Scan List
    factory<ScanListMiddleware> {
        ScanListMiddleware(
            getScansUseCase = get<GetScansUseCase>(),
            clearAllCacheUseCase = get<ClearAllCacheUseCase>()
        )
    }

    viewModel {
        ScanListViewModel(
            middleware = get()
        )
    }

    // Scan Detail - Single shared ViewModel instance
    factory<ScanDetailMiddleware> { ScanDetailMiddleware(getBathymetryUseCase = get<GetBathymetryUseCase>()) }

    // Single ViewModel shared between list and detail screens
    viewModel {
        ScanDetailViewModel(
            middleware = get()
        )
    }
}
