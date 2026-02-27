package lt.vitalijus.core.data.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import lt.vitalijus.core.data.logging.KermitLogger
import lt.vitalijus.core.data.networking.HttpClientFactory
import lt.vitalijus.core.domain.logging.DeepLogger
import org.koin.dsl.module

val coreDataModule = module {
    single<DeepLogger> { KermitLogger }

    single {
        HttpClientFactory(chirpLogger = get())
    }

    single<HttpClientEngine> { Darwin.create() }
}
