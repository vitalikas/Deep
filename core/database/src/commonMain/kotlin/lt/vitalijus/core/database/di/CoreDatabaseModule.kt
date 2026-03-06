package lt.vitalijus.core.database.di

import androidx.room.RoomDatabase
import lt.vitalijus.core.database.AppDatabase
import lt.vitalijus.core.database.repository.ScanRepositoryImpl
import lt.vitalijus.core.domain.repository.ScanRepository
import org.koin.dsl.module

// Platform-specific database builder provider
expect fun provideAppDatabase(): RoomDatabase.Builder<AppDatabase>

val coreDatabaseModule = module {
    single<AppDatabase> {
        provideAppDatabase().build()
    }

    single {
        get<AppDatabase>().userDao()
    }

    single {
        get<AppDatabase>().scanDao()
    }

    single {
        get<AppDatabase>().bathymetryDao()
    }

    single<ScanRepository> {
        ScanRepositoryImpl(
            scanDao = get(),
            bathymetryDao = get()
        )
    }
}
