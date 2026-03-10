package lt.vitalijus.feature.auth.presentation.login2

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginMiddleware2Test {

    @Test
    fun `on login click with valid credentials emits navigate effect`() = runTest {
        // Given - fake successful API
        val fakeApi = FakeAuthApi(success = true)
        val middleware = LoginMiddleware2(authApi = fakeApi)

        val effects = mutableListOf<LoginEffect2>()
        val emitEffect: suspend (LoginEffect2) -> Unit = { effects.add(it) }

        val state = LoginState2(
            email = "test@test.com",
            password = "123456"
        )

        // When
        middleware.handle(
            intent = LoginIntent2.OnLoginClicked,
            state = state,
            dispatchIntent = {},
            emitEffect = emitEffect
        )

        // Then
        assertEquals(LoginEffect2.Navigate, effects.first())
    }

    @Test
    fun `on login click with invalid credentials emits error effect`() = runTest {
        // Given - fake failing API
        val fakeApi = FakeAuthApi(success = false)
        val middleware = LoginMiddleware2(authApi = fakeApi)

        val effects = mutableListOf<LoginEffect2>()
        val emitEffect: suspend (LoginEffect2) -> Unit = { effects.add(it) }

        val state = LoginState2(
            email = "test@test.com",
            password = "wrong"
        )

        // When
        middleware.handle(
            intent = LoginIntent2.OnLoginClicked,
            state = state,
            dispatchIntent = {},
            emitEffect = emitEffect
        )

        // Then
        assertEquals(
            LoginEffect2.ShowError("Invalid credentials"),
            effects.first()
        )
    }
}
