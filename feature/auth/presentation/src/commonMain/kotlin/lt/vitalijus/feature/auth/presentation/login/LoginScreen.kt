package lt.vitalijus.feature.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lt.vitalijus.core.designsystem.icon.DeepIcons
import lt.vitalijus.core.presentation.util.DeviceConfiguration
import lt.vitalijus.core.presentation.util.currentDeviceConfiguration
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.ShowToast -> {
                    // show toast
                }

                else -> {}
            }
        }
    }

    LoginScreen(
        state = state,
        onIntent = viewModel::dispatch
    )
}

@Composable
internal fun LoginScreen(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit
) {
    val deviceConfig = currentDeviceConfiguration()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        when (deviceConfig) {
            DeviceConfiguration.TABLET_LANDSCAPE,
            DeviceConfiguration.TABLET_PORTRAIT -> {
                TabletLoginContent(
                    state = state,
                    onIntent = onIntent,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            DeviceConfiguration.MOBILE_LANDSCAPE -> {
                MobileLandscapeLoginContent(
                    state = state,
                    onIntent = onIntent,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            DeviceConfiguration.MOBILE_PORTRAIT -> {
                MobilePortraitLoginContent(
                    state = state,
                    onIntent = onIntent,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            DeviceConfiguration.DESKTOP -> {

            }
        }
    }
}

@Composable
private fun MobilePortraitLoginContent(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Brand header
        Text(
            text = "Deep",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Sign in to your account",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        LoginForm(
            state = state,
            onIntent = onIntent,
            maxWidth = 400.dp,
            showHeader = false
        )
    }
}

@Composable
private fun MobileLandscapeLoginContent(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Title
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Deep",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sign in to continue",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(48.dp))

        // Right side: Form
        Column(
            modifier = Modifier
                .weight(1f)
                .widthIn(max = 400.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompactLoginForm(
                state = state,
                onIntent = onIntent
            )
        }
    }
}

@Composable
private fun TabletLoginContent(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Brand/Welcome area
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(48.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Deep",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sign in to your account",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Right side: Login container
        Surface(
            modifier = Modifier
                .weight(1f)
                .padding(48.dp)
                .widthIn(max = 480.dp),
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(32.dp))

                CompactLoginForm(
                    state = state,
                    onIntent = onIntent,
                    showTitle = false
                )
            }
        }
    }
}

@Composable
private fun LoginForm(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    maxWidth: Dp = 400.dp,
    showHeader: Boolean = true
) {
    Column(
        modifier = Modifier.widthIn(max = maxWidth),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showHeader) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        CompactLoginForm(
            state = state,
            onIntent = onIntent,
            showTitle = false
        )
    }
}

@Composable
private fun CompactLoginForm(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    showTitle: Boolean = true
) {
    if (showTitle) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
    }

    OutlinedTextField(
        value = state.email,
        onValueChange = { onIntent(LoginIntent.OnEmailChange(it)) },
        label = { Text("Email") },
        singleLine = true,
        isError = !state.isEmailValid,
        supportingText = if (!state.isEmailValid && state.email.isNotBlank()) {
            { Text("Invalid email format") }
        } else null,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = state.password,
        onValueChange = { onIntent(LoginIntent.OnPasswordChange(it)) },
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = if (state.isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            IconButton(
                onClick = { onIntent(LoginIntent.OnTogglePasswordVisibility) }
            ) {
                Icon(
                    painter = if (state.isPasswordVisible) {
                        DeepIcons.eyeOffIcon
                    } else {
                        DeepIcons.eyeIcon
                    },
                    contentDescription = if (state.isPasswordVisible) {
                        "Hide password"
                    } else {
                        "Show password"
                    }
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Fixed height error container to prevent layout shift
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
        contentAlignment = Alignment.Center
    ) {
        state.errorMessage?.let { error ->
            Text(
                text = error.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Button(
        onClick = { onIntent(LoginIntent.OnLoginClick) },
        enabled = !state.isLoading && state.email.isNotBlank() && state.password.isNotBlank() && state.isEmailValid,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        if (state.isLoading) {
            Text(
                "Loading...",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            Text(
                "Login",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
