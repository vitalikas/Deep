package lt.vitalijus.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import lt.vitalijus.core.database.AppDatabase
import platform.Foundation.NSHomeDirectory

actual fun provideAppDatabase(): RoomDatabase.Builder<AppDatabase> {
    val dbPath = NSHomeDirectory() + "/Documents/deep.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbPath
    ).setDriver(BundledSQLiteDriver())
}
