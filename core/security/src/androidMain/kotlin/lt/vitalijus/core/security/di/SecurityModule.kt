package lt.vitalijus.core.security.di

import android.content.Context
import co.touchlab.kermit.Logger
import lt.vitalijus.core.security.AndroidTokenStorage
import lt.vitalijus.core.security.TokenStorage
import org.koin.dsl.module

val securityModule = module {
    single<TokenStorage> {
        AndroidTokenStorage(
            context = get<Context>(),
            logger = Logger.withTag("TokenStorage")
        )
    }
}
