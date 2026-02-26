package lt.vitalijus.feature.scan.data.di

import lt.vitalijus.core.data.networking.HttpClientFactory
import lt.vitalijus.feature.auth.domain.AuthRepository
import lt.vitalijus.feature.scan.data.network.ScanApiService
import lt.vitalijus.feature.scan.data.repository.ScanRepositoryImpl
import lt.vitalijus.feature.scan.domain.ScanRepository
import lt.vitalijus.feature.scan.domain.GetBathymetryUseCase
import org.koin.dsl.module

val scanDataModule = module {
    // API Service
    single {
        ScanApiService(
            httpClient = get<HttpClientFactory>().create(engine = get()),
            authRepository = get<AuthRepository>()
        )
    }

    // Repository
    single<ScanRepository> {
        ScanRepositoryImpl(
            apiService = get()
        )
    }

    // UseCases
    factory { GetBathymetryUseCase(repository = get()) }
}
