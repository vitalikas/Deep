package lt.vitalijus.deep

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import lt.vitalijus.core.data.di.coreDataModule
import lt.vitalijus.core.database.di.coreDatabaseModule
import lt.vitalijus.deep.di.appModule
import lt.vitalijus.feature.auth.data.di.authDataModule
import lt.vitalijus.feature.auth.domain.di.authDomainModule
import lt.vitalijus.feature.auth.presentation.di.authPresentationModule
import lt.vitalijus.feature.scan.data.di.scanDataModule
import lt.vitalijus.feature.scan.domain.di.scanDomainModule
import lt.vitalijus.feature.scan.presentation.di.scanPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

class DeepApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DeepApplication)
            modules(
                appModule,
                coreDataModule,
                coreDatabaseModule,
                scanDomainModule,
                scanDataModule,
                scanPresentationModule,
                authDomainModule,
                authDataModule,
                authPresentationModule
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
