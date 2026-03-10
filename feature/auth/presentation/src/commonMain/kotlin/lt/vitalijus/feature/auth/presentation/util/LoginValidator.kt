package lt.vitalijus.feature.auth.presentation.util

class LoginValidator {

    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun validate(input: String): Boolean {
        return when {
            input.isEmpty() -> false
            input.matches(emailRegex).not() -> false
            else -> true
        }
    }
}
