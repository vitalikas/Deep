package lt.vitalijus.feature.auth.presentation.login2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen2(
    state: LoginState2,
    onIntent: (LoginIntent2) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.email,
            onValueChange = { onIntent(LoginIntent2.OnEmailChanged(email = it)) },
            label = { Text("Email") },
            isError = !state.isEmailValid,
            supportingText = if (!state.isEmailValid) {
                { Text("Invalid email format") }
            } else null,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = { onIntent(LoginIntent2.OnPasswordChanged(password = it)) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("password_field")
        )

        Button(
            onClick = { onIntent(LoginIntent2.OnLoginClicked) },
            enabled = state.email.isNotBlank()
                    && state.password.isNotBlank()
                    && !state.isLoading
                    && state.isEmailValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Login")
        }

        state.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .testTag("loading_indicator")
            )
        }
    }
}
