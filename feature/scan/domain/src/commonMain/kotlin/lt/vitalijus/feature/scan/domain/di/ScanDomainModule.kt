package lt.vitalijus.feature.scan.domain.di

import lt.vitalijus.feature.scan.domain.GetBathymetryUseCase
import lt.vitalijus.feature.scan.domain.usecases.GetScansUseCase
import org.koin.dsl.module

val scanDomainModule = module {
    factory { GetScansUseCase(scanRepository = get()) }
    factory { GetBathymetryUseCase(repository = get()) }
}
