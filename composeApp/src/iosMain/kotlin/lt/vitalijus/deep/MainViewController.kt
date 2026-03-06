package lt.vitalijus.deep

import androidx.compose.ui.window.ComposeUIViewController
import lt.vitalijus.core.data.di.coreDataModule
import lt.vitalijus.core.database.di.coreDatabaseModule
import lt.vitalijus.core.security.di.securityModule
import lt.vitalijus.deep.di.appModule
import lt.vitalijus.feature.auth.data.di.authDataModule
import lt.vitalijus.feature.auth.domain.di.authDomainModule
import lt.vitalijus.feature.auth.presentation.di.authPresentationModule
import lt.vitalijus.feature.scan.data.di.scanDataModule
import lt.vitalijus.feature.scan.domain.di.scanDomainModule
import lt.vitalijus.feature.scan.presentation.di.scanPresentationModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin
    try {
        startKoin {
            modules(
                coreDatabaseModule,
                coreDataModule,
                securityModule,
                authDataModule,
                authDomainModule,
                appModule,
                scanDomainModule,
                scanDataModule,
                scanPresentationModule,
                authPresentationModule
            )
        }
    } catch (e: IllegalStateException) {
        // Koin already started, ignore
    }
    App()
}
