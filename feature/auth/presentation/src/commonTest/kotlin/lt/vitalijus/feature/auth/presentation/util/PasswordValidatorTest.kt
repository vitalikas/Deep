package lt.vitalijus.feature.auth.presentation.util

import kotlin.test.Test
import kotlin.test.assertFalse

class PasswordValidatorTest {

    @Test
    fun `empty password is invalid`() {
        val validator = PasswordValidator()

        val result = validator.validate("")

        assertFalse(result)
    }
}
