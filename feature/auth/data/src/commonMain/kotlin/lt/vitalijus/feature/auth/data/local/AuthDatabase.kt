package lt.vitalijus.feature.auth.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.datetime.Instant

@Database(
    entities = [UserEntity::class],
    version = 1
)
@TypeConverters(AuthDatabaseConverters::class)
abstract class AuthDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

class AuthDatabaseConverters {
    @TypeConverter
    fun fromInstant(value: Instant?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toInstant(value: String?): Instant? {
        return value?.let { Instant.parse(it) }
    }
}
