package lt.vitalijus.feature.auth.domain

import lt.vitalijus.core.domain.model.Scan

data class User(
    val id: Long,
    val email: String,
    val name: String? = null,
    val validTill: String? = null,
    val registrationDate: String? = null,
    val isLoggedIn: Boolean = true
)

data class LoginResult(
    val user: User,
    val token: String,
    val scans: List<Scan>
)
