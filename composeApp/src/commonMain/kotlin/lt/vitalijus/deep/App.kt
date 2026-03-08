package lt.vitalijus.deep

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import lt.vitalijus.core.designsystem.theme.DeepTheme
import lt.vitalijus.deep.app.AppIntent
import lt.vitalijus.deep.app.AppState
import lt.vitalijus.deep.app.AppViewModel
import lt.vitalijus.deep.navigation.Route
import lt.vitalijus.feature.auth.presentation.login.LoginScreenRoot
import lt.vitalijus.feature.scan.presentation.scandetail.ScanDetailScreenRoot
import lt.vitalijus.feature.scan.presentation.scans.ScanListScreenRoot
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    DeepTheme {
        val vm: AppViewModel = koinViewModel()
        val appState by vm.state.collectAsStateWithLifecycle()

        // Check auth status on app start
//        LaunchedEffect(Unit) {
//            vm.dispatch(AppIntent.CheckAuth)
//        }

        when (appState) {
            is AppState.Initializing -> SplashScreen()
            is AppState.Unauthenticated -> AuthNav(
                onLoginSuccess = {
                    vm.dispatch(AppIntent.AuthChecked(isAuthenticated = true))
                }
            )
            is AppState.Authenticated -> MainNav(
                onLogout = {
                    vm.dispatch(AppIntent.Logout)
                }
            )
        }
    }
}

@Composable
private fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun AuthNav(
    onLoginSuccess: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Login
    ) {
        composable<Route.Login> {
            LoginScreenRoot(
                onLoginSuccess = onLoginSuccess
            )
        }
    }
}

@Composable
private fun MainNav(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.ScanList()
    ) {
        composable<Route.ScanList> { backStackEntry ->
            val scanList = backStackEntry.toRoute<Route.ScanList>()
            ScanListScreenRoot(
                selectedScanId = scanList.selectedScanId,
                onScanClick = { scanId ->
                    navController.navigate(
                        Route.ScanDetail(
                            scanId = scanId,
                            scanName = "Scan #$scanId"
                        )
                    )
                },
                onLogout = onLogout
            )
        }

        composable<Route.ScanDetail> { backStackEntry ->
            val scanDetail = backStackEntry.toRoute<Route.ScanDetail>()
            ScanDetailScreenRoot(
                scanId = scanDetail.scanId,
                scanName = scanDetail.scanName,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
