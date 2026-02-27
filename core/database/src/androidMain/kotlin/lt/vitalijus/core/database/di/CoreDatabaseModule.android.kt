package lt.vitalijus.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import lt.vitalijus.core.database.AppDatabase
import org.koin.mp.KoinPlatformTools

actual fun provideAppDatabase(): RoomDatabase.Builder<AppDatabase> {
    val context = KoinPlatformTools.defaultContext().get().get<Context>()
    val dbFile = context.getDatabasePath("deep.db")
    return Room.databaseBuilder(
        context = context,
        name = dbFile.absolutePath
    )
}
