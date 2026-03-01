package lt.vitalijus.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import lt.vitalijus.core.database.AppDatabase
import org.koin.java.KoinJavaComponent.getKoin

actual fun provideAppDatabase(): RoomDatabase.Builder<AppDatabase> {
    val context = getKoin().get<android.content.Context>()
    return Room.databaseBuilder(
        context = context,
        name = "deep.db"
    )
}
