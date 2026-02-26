package lt.vitalijus.feature.auth.domain.di

import lt.vitalijus.feature.auth.domain.usecases.GetCurrentUserUseCase
import lt.vitalijus.feature.auth.domain.usecases.IsAuthenticatedUseCase
import lt.vitalijus.feature.auth.domain.usecases.LoginUseCase
import lt.vitalijus.feature.auth.domain.usecases.LogoutUseCase
import org.koin.dsl.module

val authDomainModule = module {
    factory { LoginUseCase(authRepository = get()) }
    factory { GetCurrentUserUseCase(authRepository = get()) }
    factory { IsAuthenticatedUseCase(authRepository = get()) }
    factory { LogoutUseCase(authRepository = get()) }
}
