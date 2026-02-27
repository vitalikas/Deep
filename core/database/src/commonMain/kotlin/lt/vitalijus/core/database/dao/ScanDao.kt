package lt.vitalijus.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import lt.vitalijus.core.database.entity.ScanEntity

/**
 * Room DAO for scan operations.
 */
@Dao
interface ScanDao {

    @Query("SELECT * FROM scans")
    fun getAllScans(): Flow<List<ScanEntity>>

    @Query("SELECT * FROM scans")
    suspend fun getAllScansSync(): List<ScanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScans(scans: List<ScanEntity>)

    @Query("DELETE FROM scans")
    suspend fun clearAllScans()

    @Query("SELECT * FROM scans WHERE id = :scanId")
    suspend fun getScanById(scanId: Long): ScanEntity?
}
