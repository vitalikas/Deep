package lt.vitalijus.feature.scan.data.di

import lt.vitalijus.core.data.networking.HttpClientFactory
import lt.vitalijus.core.domain.auth.TokenProvider
import lt.vitalijus.core.domain.repository.ScanRepository
import lt.vitalijus.feature.scan.data.network.ScanApiService
import lt.vitalijus.feature.scan.data.usecase.GetBathymetryUseCaseImpl
import lt.vitalijus.feature.scan.domain.usecase.GetBathymetryUseCase
import org.koin.dsl.module

val scanDataModule = module {
    // API Service
    single {
        ScanApiService(
            httpClient = get<HttpClientFactory>().create(engine = get())
        )
    }

    // UseCase - bind implementation to domain interface
    factory<GetBathymetryUseCase> {
        GetBathymetryUseCaseImpl(
            scanRepository = get<ScanRepository>(),
            apiService = get(),
            tokenProvider = get<TokenProvider>()
        )
    }
}
