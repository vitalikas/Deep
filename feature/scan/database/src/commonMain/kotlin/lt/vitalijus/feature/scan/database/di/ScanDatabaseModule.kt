package lt.vitalijus.feature.scan.database.di

import androidx.room.RoomDatabase
import lt.vitalijus.feature.scan.database.ScanDatabase
import org.koin.dsl.module

expect fun provideScanDatabase(): RoomDatabase.Builder<ScanDatabase>

val scanDatabaseModule = module {
    single {
        provideScanDatabase().build()
    }

    single {
        get<ScanDatabase>().scanDao()
    }
}
