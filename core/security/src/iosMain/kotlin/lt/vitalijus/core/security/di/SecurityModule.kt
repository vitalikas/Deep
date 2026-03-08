package lt.vitalijus.core.security.di

import co.touchlab.kermit.Logger
import lt.vitalijus.core.security.IOSTokenStorage
import lt.vitalijus.core.security.TokenStorage
import org.koin.dsl.module

val securityModule = module {
    single<TokenStorage> {
        IOSTokenStorage(
            logger = Logger.withTag("TokenStorage")
        )
    }
}
