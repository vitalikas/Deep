package lt.vitalijus.core.security.di

import co.touchlab.kermit.Logger
import lt.vitalijus.core.security.IOSSecureStorage
import lt.vitalijus.core.security.SecureStorage
import lt.vitalijus.core.security.TokenStorage
import lt.vitalijus.core.security.TokenStorageAdapter
import org.koin.dsl.module

val securityModule = module {
    single<SecureStorage> {
        IOSSecureStorage(
            logger = Logger.withTag("SecureStorage")
        )
    }

    single<TokenStorage> {
        TokenStorageAdapter(
            secureStorage = get()
        )
    }
}
