package lt.vitalijus.feature.auth.data.di

import androidx.room.RoomDatabase
import lt.vitalijus.core.data.networking.HttpClientFactory
import lt.vitalijus.core.domain.logging.DeepLogger
import lt.vitalijus.feature.auth.data.local.AuthDatabase
import lt.vitalijus.feature.auth.data.network.AuthApiService
import lt.vitalijus.feature.auth.data.network.TokenManager
import lt.vitalijus.feature.auth.data.network.TokenManagerImpl
import lt.vitalijus.feature.auth.data.repository.AuthRepositoryImpl
import lt.vitalijus.feature.auth.domain.AuthRepository
import org.koin.dsl.module

expect fun provideAuthDatabase(): RoomDatabase.Builder<AuthDatabase>

val authDataModule = module {
    // Database
    single {
        provideAuthDatabase().build()
    }

    single {
        get<AuthDatabase>().userDao()
    }

    // API Service
    single {
        AuthApiService(
            httpClient = get<HttpClientFactory>().create(engine = get())
        )
    }

    // Token Manager
    single<TokenManager> {
        TokenManagerImpl(
            userDao = get()
        )
    }

    // Repository
    single<AuthRepository> {
        AuthRepositoryImpl(
            apiService = get(),
            userDao = get(),
            tokenManager = get(),
            logger = get<DeepLogger>()
        )
    }
}
