package lt.vitalijus.feature.scan.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for storing scan data.
 */
@Entity(tableName = "scans")
data class ScanEntity(
    @PrimaryKey
    val id: Long,
    val lat: Double,
    val lon: Double,
    val name: String?,
    val date: String?,
    val scanPoints: Int,
    val mode: Int
)
