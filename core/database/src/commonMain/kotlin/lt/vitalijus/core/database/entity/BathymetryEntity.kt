package lt.vitalijus.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock

/**
 * Room entity for cached bathymetry data.
 */
@Entity(tableName = "bathymetry")
data class BathymetryEntity(
    @PrimaryKey
    @ColumnInfo(name = "scan_id")
    val scanId: Long,
    @ColumnInfo(name = "json_data")
    val jsonData: String, // Store as JSON for simplicity
    @ColumnInfo(name = "cached_at")
    val cachedAt: Long = Clock.System.now().toEpochMilliseconds()
)
