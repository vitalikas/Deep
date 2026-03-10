package lt.vitalijus.feature.auth.presentation.login2

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class LoginState2Test {

    @Test
    fun `initial state has empty fields`() {
        val state = LoginState2()

        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertFalse(state.isLoginSuccessful)
    }
}
