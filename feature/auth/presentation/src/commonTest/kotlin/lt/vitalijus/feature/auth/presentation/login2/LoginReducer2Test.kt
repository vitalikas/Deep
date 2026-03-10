package lt.vitalijus.feature.auth.presentation.login2

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginReducer2Test {

    @Test
    fun `on email change updates email state`() {
        // Given
        val initialState = LoginState2()
        val reducer = createLoginReducer2()

        // When
        val newState = reducer.reduce(
            state = initialState,
            intent = LoginIntent2.OnEmailChanged(email = "test@example.com")
        )

        // Then
        assertEquals("test@example.com", newState.email)
    }

    @Test
    fun `on password change updates password state`() {
        // Given
        val initialState = LoginState2()
        val reducer = createLoginReducer2()

        // When
        val newState = reducer.reduce(
            state = initialState,
            intent = LoginIntent2.OnPasswordChanged(password = "password123")
        )

        // Then
        assertEquals("password123", newState.password)
    }

    @Test
    fun `on login click sets loading to true`() {
        // Given
        val initialState = LoginState2(
            email = "test@test.com",
            password = "123456"
        )
        val reducer = createLoginReducer2()

        // When
        val newState = reducer.reduce(
            state = initialState,
            intent = LoginIntent2.OnLoginClicked
        )

        // Then
        assertTrue(newState.isLoading)
    }
}
