package lt.vitalijus.feature.auth.data.di

import lt.vitalijus.core.data.networking.HttpClientFactory
import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.domain.auth.TokenProvider
import lt.vitalijus.core.domain.logging.DeepLogger
import lt.vitalijus.core.domain.repository.ScanRepository
import lt.vitalijus.core.security.SecureStorage
import lt.vitalijus.feature.auth.data.adapter.TokenProviderAdapter
import lt.vitalijus.feature.auth.data.network.AuthApiService
import lt.vitalijus.feature.auth.data.network.TokenManager
import lt.vitalijus.feature.auth.data.network.TokenManagerImpl
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

    // Token Manager (uses SecureStorage for token, UserDao for user data)
    single<TokenManager> {
        TokenManagerImpl(
            secureStorage = get<SecureStorage>(),
            userDao = get<UserDao>()
        )
    }

    // Repository
    single<AuthRepository> {
        AuthRepositoryImpl(
            apiService = get(),
            userDao = get<UserDao>(),
            tokenManager = get(),
            scanRepository = get<ScanRepository>(),
            logger = get<DeepLogger>()
        )
    }

    // Token Provider (uses both SecureStorage and UserDao)
    single<TokenProvider> {
        TokenProviderAdapter(
            tokenManager = get<TokenManager>(),
            userDao = get<UserDao>()
        )
    }
}
