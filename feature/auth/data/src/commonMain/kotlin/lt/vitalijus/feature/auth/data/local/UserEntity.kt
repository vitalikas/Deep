package lt.vitalijus.feature.auth.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Long,
    val email: String,
    val name: String? = null,
    val token: String,
    val validTill: String? = null,
    val registrationDate: String? = null,
    val isLoggedIn: Boolean = true
)
