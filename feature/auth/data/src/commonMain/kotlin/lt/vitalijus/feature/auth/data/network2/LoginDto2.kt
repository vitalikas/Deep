package lt.vitalijus.feature.auth.data.network2

data class LoginResponseDto2(
    val login: LoginDataDto2? = null,
    val user: UserDto2? = null,
    val scans: List<ScanDto2>? = null
)

data class LoginDataDto2(
    val token: String,
    val userId: Long,
    val validated: Boolean,
    val validTill: String? = null,
    val registrationDate: String? = null
)

data class UserDto2(
    val id: Long? = null,
    val email: String? = null,
    val name: String? = null
)

data class ScanDto2(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val name: String? = null,
    val date: String? = null,
    val scanPoints: Int,
    val mode: Int
)
