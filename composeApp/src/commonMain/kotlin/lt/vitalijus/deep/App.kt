package lt.vitalijus.deep

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lt.vitalijus.core.designsystem.theme.DeepTheme
import lt.vitalijus.deep.navigation.Route
import lt.vitalijus.feature.auth.presentation.login.LoginScreenRoot
import lt.vitalijus.feature.scan.presentation.scans.ScanListScreenRoot

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
                    onNavigateToScans = {
                        navController.navigate(Route.ScanList) {
                            popUpTo(Route.Login) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<Route.ScanList> {
                ScanListScreenRoot(
                    onScanClick = { scanId ->
                        // Navigate to scan detail
                    }
                )
            }
        }
    }
}
