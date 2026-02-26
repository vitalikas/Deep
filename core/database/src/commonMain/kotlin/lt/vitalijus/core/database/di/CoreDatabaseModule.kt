package lt.vitalijus.core.database.di

import androidx.room.RoomDatabase
import lt.vitalijus.core.database.AppDatabase
import lt.vitalijus.core.database.dao.ScanDao
import lt.vitalijus.core.database.dao.UserDao
import org.koin.dsl.module

expect fun provideAppDatabase(): RoomDatabase.Builder<AppDatabase>

val coreDatabaseModule = module {
    single {
        provideAppDatabase().build()
    }

    single<UserDao> {
        get<AppDatabase>().userDao()
    }

    single<ScanDao> {
        get<AppDatabase>().scanDao()
    }
}
