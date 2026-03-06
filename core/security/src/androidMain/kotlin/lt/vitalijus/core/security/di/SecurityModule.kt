package lt.vitalijus.core.security.di

import android.content.Context
import co.touchlab.kermit.Logger
import lt.vitalijus.core.security.AndroidSecureStorage
import lt.vitalijus.core.security.SecureStorage
import org.koin.dsl.module

val securityModule = module {

    single<SecureStorage> {
        AndroidSecureStorage(
            context = get<Context>(),
            logger = Logger.withTag("SecureStorage")
        )
    }
}
