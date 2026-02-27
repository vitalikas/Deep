package lt.vitalijus.feature.scan.domain.di

import lt.vitalijus.feature.scan.domain.usecase.ClearAllCacheUseCase
import lt.vitalijus.feature.scan.domain.usecase.GetBathymetryUseCase
import lt.vitalijus.feature.scan.domain.usecase.GetScansUseCase
import org.koin.dsl.module

val scanDomainModule = module {
    factory { GetScansUseCase(scanRepository = get()) }
    factory { GetBathymetryUseCase(repository = get()) }
    factory { ClearAllCacheUseCase(scanRepository = get()) }
}
