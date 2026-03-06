package lt.vitalijus.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import lt.vitalijus.core.database.entity.UserEntity

/**
 * Room DAO for user operations.
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getCurrentUserSync(): UserEntity?

    @Query("SELECT token FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getCurrentToken(): String?

    @Query("UPDATE users SET isLoggedIn = 0 WHERE id = :userId")
    suspend fun logout(userId: Long)

    @Query("DELETE FROM users")
    suspend fun clearAllUsers()

    @Query("UPDATE users SET token = :newToken WHERE id = :userId")
    suspend fun updateToken(
        userId: Long,
        newToken: String
    )
}
