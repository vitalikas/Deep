package lt.vitalijus.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalijus.core.database.entity.BathymetryEntity

/**
 * Room DAO for bathymetry cache operations.
 */
@Dao
interface BathymetryDao {

    @Query("SELECT * FROM bathymetry WHERE scan_id = :scanId")
    suspend fun getByScanId(scanId: Long): BathymetryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bathymetry: BathymetryEntity)

    @Query("DELETE FROM bathymetry WHERE scan_id = :scanId")
    suspend fun deleteByScanId(scanId: Long)

    @Query("DELETE FROM bathymetry WHERE cached_at < :timestamp")
    suspend fun deleteOlderThan(timestamp: Long)

    @Query("DELETE FROM bathymetry")
    suspend fun clearAll()
}
