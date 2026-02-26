package lt.vitalijus.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import lt.vitalijus.core.database.dao.ScanDao
import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.database.entity.ScanEntity
import lt.vitalijus.core.database.entity.UserEntity

/**
 * Main application database.
 * Contains all entities from different features.
 */
@Database(
    entities = [
        UserEntity::class,
        ScanEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun scanDao(): ScanDao
}
