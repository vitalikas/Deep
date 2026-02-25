package lt.vitalijus.deep

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lt.vitalijus.core.designsystem.theme.DeepTheme
import lt.vitalijus.deep.navigation.Route
import lt.vitalijus.deep.presentation.scanlist.ScanListScreenRoot
import lt.vitalijus.feature.auth.presentation.login.LoginScreenRoot

@Composable
fun App() {
    DeepTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Route.Login
        ) {
            composable<Route.Login> {
                LoginScreenRoot(
                    onLoginSuccess = {
                        navController.navigate(Route.ScanList) {
                            popUpTo(Route.Login) { inclusive = true }
                        }
                    }
                )
            }

            composable<Route.ScanList> {
                ScanListScreenRoot(
                    onLogout = {
                        navController.navigate(Route.Login) {
                            popUpTo(Route.ScanList) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
