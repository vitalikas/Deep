package lt.vitalijus.feature.auth.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import lt.vitalijus.feature.auth.data.local.AuthDatabase
import org.koin.mp.KoinPlatformTools

actual fun provideAuthDatabase(): RoomDatabase.Builder<AuthDatabase> {
    val context = KoinPlatformTools.defaultContext().get().get<Context>()
    val dbFile = context.getDatabasePath("auth.db")
    return Room.databaseBuilder(
        context = context,
        name = dbFile.absolutePath
    )
}
