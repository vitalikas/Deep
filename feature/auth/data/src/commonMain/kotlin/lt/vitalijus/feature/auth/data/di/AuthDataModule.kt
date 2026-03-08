package lt.vitalijus.feature.auth.data.di

import lt.vitalijus.core.data.networking.HttpClientFactory
import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.domain.auth.TokenProvider
import lt.vitalijus.core.domain.logging.DeepLogger
import lt.vitalijus.core.domain.repository.ScanRepository
import lt.vitalijus.core.security.TokenStorage
import lt.vitalijus.feature.auth.data.adapter.TokenProviderAdapter
import lt.vitalijus.feature.auth.data.auth.AuthStateManager
import lt.vitalijus.feature.auth.data.auth.AuthStateManagerImpl
import lt.vitalijus.feature.auth.data.network.AuthApiService
import lt.vitalijus.feature.auth.data.repository.AuthRepositoryImpl
import lt.vitalijus.feature.auth.domain.AuthRepository
import org.koin.dsl.module

val authDataModule = module {
    // API Service
    single {
        AuthApiService(
            httpClient = get<HttpClientFactory>().create(engine = get())
        )
    }

    // Auth State Manager (combines TokenStorage + UserDao)
    single<AuthStateManager> {
        AuthStateManagerImpl(
            tokenStorage = get<TokenStorage>(),
            userDao = get<UserDao>()
        )
    }

    // Repository
    single<AuthRepository> {
        AuthRepositoryImpl(
            apiService = get(),
            userDao = get<UserDao>(),
            tokenStorage = get<TokenStorage>(),
            scanRepository = get<ScanRepository>(),
            logger = get<DeepLogger>()
        )
    }

    // Token Provider (uses TokenStorage directly)
    single<TokenProvider> {
        TokenProviderAdapter(
            tokenStorage = get<TokenStorage>(),
            userDao = get<UserDao>()
        )
    }
}
