package lt.vitalijus.feature.auth.domain

import lt.vitalijus.core.domain.model.Scan

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
    val scans: List<Scan>
)
