package lt.vitalijus.core.security.di

import co.touchlab.kermit.Logger
import lt.vitalijus.core.security.IOSSecureStorage
import lt.vitalijus.core.security.SecureStorage
import org.koin.dsl.module

val securityModule = module {

    single<SecureStorage> {
        IOSSecureStorage(
            logger = Logger.withTag("SecureStorage")
        )
    }
}
