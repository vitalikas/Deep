package lt.vitalijus.deep

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import lt.vitalijus.core.designsystem.theme.DeepTheme
import lt.vitalijus.core.presentation.util.DeviceConfiguration
import lt.vitalijus.core.presentation.util.currentDeviceConfiguration
import lt.vitalijus.deep.app.AppIntent
import lt.vitalijus.deep.app.AppState
import lt.vitalijus.deep.app.AppViewModel
import lt.vitalijus.deep.navigation.PersistedScanDetail
import lt.vitalijus.deep.navigation.Route
import lt.vitalijus.feature.auth.presentation.login.LoginScreenRoot
import lt.vitalijus.feature.scan.presentation.scandetail.ScanDetailScreenRoot
import lt.vitalijus.feature.scan.presentation.scans.ScanListScreenRoot
import org.koin.compose.viewmodel.koinViewModel

private val persistedScanDetailSaver = mapSaver<PersistedScanDetail?>(
    save = { detail ->
        detail?.let {
            mapOf(
                "scanId" to it.scanId,
                "scanName" to it.scanName
            )
        } ?: emptyMap()
    },
    restore = { values ->
        val scanId = values["scanId"] as? Long ?: return@mapSaver null
        val scanName = values["scanName"] as? String ?: return@mapSaver null
        PersistedScanDetail(
            scanId = scanId,
            scanName = scanName
        )
    }
)

@Composable
fun App() {
    DeepTheme {
        val appViewModel: AppViewModel = koinViewModel()
        val appState by appViewModel.state.collectAsStateWithLifecycle()

        var persistedScanDetail by rememberSaveable(stateSaver = persistedScanDetailSaver) {
            mutableStateOf(null)
        }

        when (appState) {
            is AppState.Initializing -> SplashScreen()

            is AppState.Unauthenticated -> AuthNav(
                onLoginSuccess = {
                    appViewModel.dispatch(intent = AppIntent.AuthChecked(isAuthenticated = true))
                }
            )

            is AppState.Authenticated -> MainNav(
                persistedScanDetail = persistedScanDetail,
                onPersistedScanDetailChange = { detail ->
                    persistedScanDetail = detail
                },
                onLogout = {
                    persistedScanDetail = null
                    appViewModel.dispatch(intent = AppIntent.Logout)
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
    persistedScanDetail: PersistedScanDetail?,
    onPersistedScanDetailChange: (PersistedScanDetail?) -> Unit,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    var suppressAutoOpen by rememberSaveable { mutableStateOf(false) }
    var wasOnDetail by rememberSaveable { mutableStateOf(false) }

    val deviceConfig = currentDeviceConfiguration()

    // Detect leaving detail in portrait (including system back gesture)
    LaunchedEffect(backStackEntry, deviceConfig) {
        val isOnDetail = runCatching {
            backStackEntry?.toRoute<Route.ScanDetail>()
            true
        }.getOrDefault(false)

        if (wasOnDetail && !isOnDetail && deviceConfig == DeviceConfiguration.MOBILE_PORTRAIT) {
            suppressAutoOpen = true
            onPersistedScanDetailChange(null)
        }
        wasOnDetail = isOnDetail
    }

    // Re-open detail in portrait only when it was not explicitly dismissed via Back
    LaunchedEffect(persistedScanDetail, deviceConfig, backStackEntry, suppressAutoOpen) {
        val detail = persistedScanDetail ?: return@LaunchedEffect
        if (suppressAutoOpen || deviceConfig != DeviceConfiguration.MOBILE_PORTRAIT) return@LaunchedEffect

        val isOnDetail = runCatching {
            backStackEntry?.toRoute<Route.ScanDetail>()
            true
        }.getOrDefault(false)

        if (!isOnDetail) {
            navController.navigate(
                Route.ScanDetail(
                    scanId = detail.scanId,
                    scanName = detail.scanName.ifBlank { "Scan #${detail.scanId}" }
                )
            ) {
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Route.ScanList()
    ) {
        composable<Route.ScanList> { backStackEntry ->
            val scanList = backStackEntry.toRoute<Route.ScanList>()

            ScanListScreenRoot(
                selectedScanId = scanList.selectedScanId,
                onScanClick = { scanId ->
                    val targetRoute = Route.ScanDetail(
                        scanId = scanId,
                        scanName = "Scan #$scanId"
                    )
                    suppressAutoOpen = false
                    onPersistedScanDetailChange(
                        PersistedScanDetail(
                            scanId = targetRoute.scanId,
                            scanName = targetRoute.scanName
                        )
                    )
                    navController.navigate(targetRoute) {
                        launchSingleTop = true
                    }
                },
                onSelectedScanChange = { selectedId, selectedName ->
                    if (selectedId != null && deviceConfig != DeviceConfiguration.MOBILE_PORTRAIT) {
                        onPersistedScanDetailChange(
                            PersistedScanDetail(
                                scanId = selectedId,
                                scanName = selectedName
                            )
                        )
                    }
                },
                onLogout = onLogout
            )
        }

        composable<Route.ScanDetail> { backStackEntry ->
            val scanDetail = backStackEntry.toRoute<Route.ScanDetail>()
            onPersistedScanDetailChange(
                PersistedScanDetail(
                    scanId = scanDetail.scanId,
                    scanName = scanDetail.scanName
                )
            )
            ScanDetailScreenRoot(
                scanId = scanDetail.scanId,
                scanName = scanDetail.scanName,
                onNavigateBack = {
                    if (deviceConfig == DeviceConfiguration.MOBILE_PORTRAIT) {
                        suppressAutoOpen = true
                        onPersistedScanDetailChange(null)
                        navController.popBackStack()
                    } else {
                        onPersistedScanDetailChange(
                            PersistedScanDetail(
                                scanId = scanDetail.scanId,
                                scanName = scanDetail.scanName
                            )
                        )
                        navController.navigate(Route.ScanList(selectedScanId = scanDetail.scanId)) {
                            popUpTo(scanDetail) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
