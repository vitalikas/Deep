package lt.vitalijus.feature.auth.presentation.login2

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class LoginScreen2Test {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun login_screen_displays_email_field() {
        // When
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(),
                onIntent = {}
            )
        }

        // Then
        composeTestRule
            .onNodeWithText("Email")
            .assertExists()
    }

    @Test
    fun login_screen_displays_password_field() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(),
                onIntent = {}
            )
        }

        composeTestRule
            .onNodeWithText("Password")
            .assertExists()
    }

    @Test
    fun login_screen_displays_login_button() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(),
                onIntent = {}
            )
        }

        composeTestRule
            .onNodeWithText("Login")
            .assertExists()
    }

}
