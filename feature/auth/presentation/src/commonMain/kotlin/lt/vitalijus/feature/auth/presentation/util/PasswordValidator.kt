package lt.vitalijus.feature.auth.presentation.util

class PasswordValidator {
    fun validate(input: String): Boolean {
        return when {
            input.isEmpty() -> false
            else -> true
        }
    }
}
