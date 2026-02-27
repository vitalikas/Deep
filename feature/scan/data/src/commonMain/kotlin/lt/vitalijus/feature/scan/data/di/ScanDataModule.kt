package lt.vitalijus.feature.scan.data.di

import lt.vitalijus.core.data.networking.HttpClientFactory
import lt.vitalijus.core.database.dao.BathymetryDao
import lt.vitalijus.core.database.dao.ScanDao
import lt.vitalijus.feature.auth.domain.AuthRepository
import lt.vitalijus.feature.scan.data.network.ScanApiService
import lt.vitalijus.feature.scan.data.repository.ScanRepositoryImpl
import lt.vitalijus.feature.scan.domain.repository.ScanRepository
import lt.vitalijus.feature.scan.domain.usecase.GetBathymetryUseCase
import org.koin.dsl.module

val scanDataModule = module {
    // API Service
    single {
        ScanApiService(
            httpClient = get<HttpClientFactory>().create(engine = get())
        )
    }

    // Repository
    single<ScanRepository> {
        ScanRepositoryImpl(
            apiService = get(),
            authRepository = get<AuthRepository>(),
            scanDao = get<ScanDao>(),
            bathymetryDao = get<BathymetryDao>()
        )
    }

    // UseCases
    factory { GetBathymetryUseCase(repository = get()) }
}
