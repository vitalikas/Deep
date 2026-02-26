package lt.vitalijus.feature.auth.domain

/**
 * Lightweight scan info for passing scan data after login.
 * The caller should map this to full Scan model.
 */
data class ScanInfo(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val name: String?,
    val date: String?,
    val scanPoints: Int,
    val mode: Int
)

data class User(
    val id: Long,
    val email: String,
    val name: String? = null,
    val token: String,
    val validTill: String? = null,
    val registrationDate: String? = null,
    val isLoggedIn: Boolean = true
)

data class LoginResult(
    val user: User,
    val scans: List<ScanInfo>
)
