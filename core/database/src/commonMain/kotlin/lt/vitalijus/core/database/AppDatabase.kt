package lt.vitalijus.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import lt.vitalijus.core.database.dao.BathymetryDao
import lt.vitalijus.core.database.dao.ScanDao
import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.database.entity.BathymetryEntity
import lt.vitalijus.core.database.entity.ScanEntity
import lt.vitalijus.core.database.entity.UserEntity

/**
 * Main application database.
 * Contains all entities from different features.
 */
@Database(
    entities = [
        UserEntity::class,
        ScanEntity::class,
        BathymetryEntity::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun scanDao(): ScanDao
    abstract fun bathymetryDao(): BathymetryDao
}
