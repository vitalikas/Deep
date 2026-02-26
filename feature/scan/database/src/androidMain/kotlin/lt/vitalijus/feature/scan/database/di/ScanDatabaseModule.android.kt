package lt.vitalijus.feature.scan.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import lt.vitalijus.feature.scan.database.ScanDatabase
import org.koin.mp.KoinPlatformTools

actual fun provideScanDatabase(): RoomDatabase.Builder<ScanDatabase> {
    val context = KoinPlatformTools.defaultContext().get().get<Context>()
    val dbFile = context.getDatabasePath("scan.db")
    return Room.databaseBuilder(
        context = context,
        name = dbFile.absolutePath
    )
}
