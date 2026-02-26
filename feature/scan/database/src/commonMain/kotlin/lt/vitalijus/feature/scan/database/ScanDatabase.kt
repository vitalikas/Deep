package lt.vitalijus.feature.scan.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database for scan feature.
 */
@Database(
    entities = [ScanEntity::class],
    version = 1
)
abstract class ScanDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao
}
