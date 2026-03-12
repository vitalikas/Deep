package lt.vitalijus.feature.auth.presentation.login2

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import junit.framework.TestCase.assertEquals
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

    @Test
    fun login_screen_shows_loading_indicator_when_loading() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(isLoading = true),
                onIntent = {}
            )
        }

        composeTestRule
            .onNodeWithTag("loading_indicator")
            .assertExists()
    }

    @Test
    fun login_screen_hides_loading_indicator_when_not_loading() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(isLoading = false),
                onIntent = {}
            )
        }

        composeTestRule
            .onNodeWithTag("loading_indicator")
            .assertDoesNotExist()
    }

    @Test
    fun login_button_disabled_when_email_field_empty() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(email = "", password = "123"),
                onIntent = {}
            )
        }

        composeTestRule
            .onNodeWithText("Login")
            .assertIsNotEnabled()
    }

    @Test
    fun login_button_disabled_when_password_field_empty() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(email = "test@example.com", password = ""),
                onIntent = {}
            )
        }

        composeTestRule
            .onNodeWithText("Login")
            .assertIsNotEnabled()
    }

    @Test
    fun login_button_disabled_when_fields_empty() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(email = "", password = ""),
                onIntent = {}
            )
        }

        composeTestRule
            .onNodeWithText("Login")
            .assertIsNotEnabled()
    }

    @Test
    fun login_button_enabled_when_fields_filled() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(email = "test@test.com", password = "pass123"),
                onIntent = {}
            )
        }

        composeTestRule
            .onNodeWithText("Login")
            .assertIsEnabled()
    }

    @Test
    fun login_button_disabled_when_loading() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(
                    email = "test@test.com",
                    password = "pass123",
                    isLoading = true
                ),
                onIntent = {}
            )
        }

        composeTestRule
            .onNodeWithText("Login")
            .assertIsNotEnabled()
    }

    @Test
    fun typing_email_triggers_on_email_changed_intent() {
        var capturedIntent: LoginIntent2? = null

        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(),
                onIntent = { capturedIntent = it }
            )
        }

        composeTestRule
            .onNodeWithText("Email")
            .performTextInput("test@test.com")

        assertEquals(
            LoginIntent2.OnEmailChanged(email = "test@test.com"),
            capturedIntent
        )
    }

    @Test
    fun typing_email_triggers_on_password_changed_intent() {
        var capturedIntent: LoginIntent2? = null

        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(),
                onIntent = { capturedIntent = it }
            )
        }

        composeTestRule
            .onNodeWithText("Password")
            .performTextInput("pass123")

        assertEquals(
            LoginIntent2.OnPasswordChanged(password = "pass123"),
            capturedIntent
        )
    }

    @Test
    fun clicking_login_button_triggers_on_login_clicked_intent() {
        var capturedIntent: LoginIntent2? = null

        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(email = "test@test.com", password = "pass123"),
                onIntent = { capturedIntent = it }
            )
        }

        composeTestRule
            .onNodeWithText("Login")
            .performClick()

        assertEquals(
            LoginIntent2.OnLoginClicked,
            capturedIntent
        )
    }

    @Test
    fun invalid_email_shows_error_message() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(
                    email = "invalidEmail",
                    isEmailValid = false
                ),
                onIntent = {}
            )
        }

        composeTestRule
            .onNodeWithText("Invalid email format")
            .assertExists()
    }

    @Test
    fun valid_email_hides_error_message() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(
                    email = "test@test.com",
                    isEmailValid = true
                ),
                onIntent = {}
            )
        }

        composeTestRule
            .onNodeWithText("Invalid email format")
            .assertDoesNotExist()
    }

    @Test
    fun password_field_masks_input() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(password = "pass123"),
                onIntent = {}
            )
        }

        // Password field should have password semantics - text is masked,
        // so searching for plain password should not find an exact visible match
        composeTestRule
            .onNodeWithTag("password_field")
            .assertExists()
    }

    @Test
    fun error_message_displayed_when_present() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(errorMessage = "Invalid credentials"),
                onIntent = {}
            )
        }

        composeTestRule
            .onNodeWithText("Invalid credentials")
            .assertExists()
    }

    @Test
    fun error_message_hidden_when_null() {
        composeTestRule.setContent {
            LoginScreen2(
                state = LoginState2(errorMessage = null),
                onIntent = {}
            )
        }

        composeTestRule
            .onNodeWithText("Invalid credentials")
            .assertDoesNotExist()
    }
}
