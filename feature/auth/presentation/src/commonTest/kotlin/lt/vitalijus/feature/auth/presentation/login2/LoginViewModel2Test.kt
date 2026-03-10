package lt.vitalijus.feature.auth.presentation.login2

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginViewModel2Test {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewModel integration - full login flow updates state`() = runTest {
        // Given
        val viewModel = LoginViewModel2(
            middleware = LoginMiddleware2(authApi = FakeAuthApi(success = true))
        )

        // When - full user flow
        viewModel.dispatch(LoginIntent2.OnEmailChanged("user@test.com"))
        viewModel.dispatch(LoginIntent2.OnPasswordChanged("pass123"))
        viewModel.dispatch(LoginIntent2.OnLoginClicked)
        advanceUntilIdle()

        // Then - check state (synchronous, no SharedFlow timing issues)
        assertEquals("user@test.com", viewModel.currentState.email)
        assertEquals("pass123", viewModel.currentState.password)
        assertTrue(viewModel.currentState.isLoading)
    }
}
