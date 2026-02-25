package lt.vitalijus.feature.auth.domain

data class User(
    val id: Long,
    val email: String,
    val name: String? = null,
    val token: String,
    val validTill: String? = null,
    val registrationDate: String? = null,
    val isLoggedIn: Boolean = true
)

data class Scan(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val name: String? = null,
    val date: String? = null,
    val scanPoints: Int,
    val mode: Int
)

data class LoginResult(
    val user: User,
    val scans: List<Scan>
)
