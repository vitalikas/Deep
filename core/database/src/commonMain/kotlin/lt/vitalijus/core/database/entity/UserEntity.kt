package lt.vitalijus.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for user data.
 *
 * Note: Authentication token is NOT stored here.
 * Token is stored in SecureStorage (Android: Encrypted DataStore, iOS: Keychain)
 * for security reasons.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Long,
    val email: String,
    val name: String? = null,
    val validTill: String? = null,
    val registrationDate: String? = null,
    val isLoggedIn: Boolean = true
)
