package lt.vitalijus.feature.auth.presentation.util

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoginValidatorTest {

    @Test
    fun `empty email is invalid`() {
        val validator = LoginValidator()

        val result = validator.validate("")

        assertFalse(result)
    }

    @Test
    fun `invalid email format is invalid`() {
        val validator = LoginValidator()

        val result = validator.validate("my.emailgmail.com")

        assertFalse(result)
    }

    @Test
    fun `valid email passes validation`() {
        val validator = LoginValidator()

        val result = validator.validate("my.email@gmail.com")

        assertTrue(result)
    }
}
