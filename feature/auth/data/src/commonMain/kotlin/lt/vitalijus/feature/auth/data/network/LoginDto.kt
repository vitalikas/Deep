package lt.vitalijus.feature.auth.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponseDto(
    val login: LoginDataDto? = null,
    val user: UserDto? = null,
    val scans: List<ScanDto>? = null
)

@Serializable
data class LoginDataDto(
    @SerialName("appId")
    val appId: String? = null,
    @SerialName("token")
    val token: String,
    @SerialName("userId")
    val userId: Long,
    @SerialName("validated")
    val validated: Boolean,
    @SerialName("validTill")
    val validTill: String? = null,
    @SerialName("registrationDate")
    val registrationDate: String? = null
)

@Serializable
data class UserDto(
    // User fields can be added here as needed
    val id: Long? = null,
    val email: String? = null,
    val name: String? = null
)

@Serializable
data class ScanDto(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val name: String? = null,
    val date: String? = null,
    @SerialName("scanPoints")
    val scanPoints: Int,
    val mode: Int
)
