@file:OptIn(ExperimentalMaterial3Api::class)

package lt.vitalijus.feature.scan.presentation.scans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lt.vitalijus.core.designsystem.icon.DeepIcons
import lt.vitalijus.core.presentation.util.DeviceConfiguration
import lt.vitalijus.core.presentation.util.currentDeviceConfiguration
import lt.vitalijus.feature.scan.presentation.generated.resources.Res
import lt.vitalijus.feature.scan.presentation.generated.resources.logout
import lt.vitalijus.feature.scan.presentation.generated.resources.open_full_screen
import lt.vitalijus.feature.scan.presentation.generated.resources.scan_empty_description
import lt.vitalijus.feature.scan.presentation.generated.resources.scan_empty_title
import lt.vitalijus.feature.scan.presentation.generated.resources.scan_list_title
import lt.vitalijus.feature.scan.presentation.generated.resources.scan_points
import lt.vitalijus.feature.scan.presentation.generated.resources.scan_points_format
import lt.vitalijus.feature.scan.presentation.generated.resources.select_scan_hint
import lt.vitalijus.feature.scan.presentation.generated.resources.view_bathymetry_map
import lt.vitalijus.feature.scan.presentation.model.toWrapperList
import lt.vitalijus.feature.scan.presentation.scandetail.BathymetryMap
import lt.vitalijus.feature.scan.presentation.scandetail.ScanDetailIntent
import lt.vitalijus.feature.scan.presentation.scandetail.ScanDetailState
import lt.vitalijus.feature.scan.presentation.scandetail.ScanDetailViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * Root composable for scan list.
 * Handles data flow from ViewModels.
 */
@Composable
fun ScanListScreenRoot(
    selectedScanId: Long? = null,
    viewModel: ScanListViewModel = koinViewModel(),
    scanDetailViewModel: ScanDetailViewModel = koinViewModel(),
    onScanClick: (Long) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val scanListState by viewModel.state.collectAsStateWithLifecycle()
    val scanDetailState by scanDetailViewModel.state.collectAsStateWithLifecycle()

    // Load scans on first composition
    LaunchedEffect(Unit) {
        if (scanListState.scans.isEmpty() && !scanListState.isLoading) {
            viewModel.dispatch(ScanListIntent.LoadScans)
        }
    }

    // Handle navigation effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ScanListEffect.NavigateToScanDetail -> onScanClick(effect.scanId)
                is ScanListEffect.ShowToast -> { /* Show toast */
                }

                is ScanListEffect.LogoutRequested -> {
                    onLogout()
                }
            }
        }
    }

    LaunchedEffect(selectedScanId) {
        if (scanListState.selectedScanId == null && selectedScanId != null) {
            viewModel.dispatch(ScanListIntent.OnSelectScan(selectedScanId))
        }
    }

    ScanListScreen(
        scanListState = scanListState,
        scanDetailState = scanDetailState,
        onScanListIntent = viewModel::dispatch,
        onScanDetailIntent = scanDetailViewModel::dispatch
    )
}

/**
 * Main scan list screen.
 * Switches between portrait and two-pane layouts based on device configuration.
 */
@Composable
internal fun ScanListScreen(
    scanListState: ScanListState,
    scanDetailState: ScanDetailState,
    onScanListIntent: (ScanListIntent) -> Unit,
    onScanDetailIntent: (ScanDetailIntent) -> Unit
) {
    val deviceConfig = currentDeviceConfiguration()

    val selectedScanId = scanListState.selectedScanId

    // Find selected scan data
    val selectedScan by remember(scanListState.scans, selectedScanId) {
        derivedStateOf {
            selectedScanId?.let { id -> scanListState.scans.find { it.id == id } }
        }
    }

    // Load detail data when in two-pane mode with selected scan
    LaunchedEffect(deviceConfig, selectedScan) {
        val scan = selectedScan
        if (deviceConfig != DeviceConfiguration.MOBILE_PORTRAIT && scan != null) {
            onScanDetailIntent(
                ScanDetailIntent.LoadScan(
                    scanId = scan.id,
                    scanName = scan.name
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.scan_list_title)) },
                actions = {
                    IconButton(onClick = { onScanListIntent(ScanListIntent.OnLogoutClick) }) {
                        Icon(
                            painter = DeepIcons.logOutIcon,
                            contentDescription = stringResource(Res.string.logout)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (deviceConfig) {
            DeviceConfiguration.MOBILE_PORTRAIT -> {
                OnePaneContent(
                    scans = scanListState.scans,
                    isLoading = scanListState.isLoading,
                    selectedScanId = selectedScanId,
                    onScanClick = { scanId ->
                        onScanListIntent(ScanListIntent.OnScanClick(scanId = scanId))
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> {
                TwoPaneContent(
                    scans = scanListState.scans,
                    isLoading = scanListState.isLoading,
                    selectedScanId = selectedScanId,
                    onScanClick = { scanId ->
                        onScanListIntent(ScanListIntent.OnSelectScan(scanId = scanId))
                    },
                    scanDetailState = scanDetailState,
                    onNavigateClick = { scanId ->
                        onScanListIntent(ScanListIntent.OnScanClick(scanId = scanId))
                    },
                    isWideScreen = deviceConfig != DeviceConfiguration.MOBILE_LANDSCAPE,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

/**
 * Reusable scan list content.
 * Used in both portrait and two-pane layouts.
 */
@Composable
private fun OnePaneContent(
    scans: List<ScanUiModel>,
    isLoading: Boolean,
    selectedScanId: Long?,
    onScanClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading && scans.isEmpty() -> {
                CircularProgressIndicator(modifier = Modifier.testTag("loadingIndicator"))
            }

            scans.isEmpty() -> {
                EmptyScansView()
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        items = scans,
                        key = { it.id }
                    ) { scan ->
                        ScanItem(
                            scan = scan,
                            isSelected = scan.id == selectedScanId,
                            onClick = { onScanClick(scan.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Two-pane layout with list and detail side by side.
 */
@Composable
private fun TwoPaneContent(
    scans: List<ScanUiModel>,
    isLoading: Boolean,
    selectedScanId: Long?,
    onScanClick: (Long) -> Unit,
    scanDetailState: ScanDetailState,
    onNavigateClick: (Long) -> Unit,
    isWideScreen: Boolean,
    modifier: Modifier = Modifier
) {
    val listWeight = if (isWideScreen) 0.3f else 0.4f
    val detailWeight = if (isWideScreen) 0.7f else 0.6f

    val selectedScan by remember(scans, selectedScanId) {
        derivedStateOf {
            selectedScanId?.let { id -> scans.find { it.id == id } }
        }
    }

    Row(modifier = modifier.fillMaxSize()) {
        // Left: Scan list
        Column(
            modifier = Modifier
                .weight(listWeight)
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            OnePaneContent(
                scans = scans,
                isLoading = isLoading,
                selectedScanId = selectedScanId,
                onScanClick = onScanClick,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        // Divider
        Spacer(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.outlineVariant)
        )

        // Right: Detail panel
        Box(
            modifier = Modifier
                .weight(detailWeight)
                .fillMaxHeight()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                selectedScan == null -> {
                    EmptySelectionView(isWideScreen = isWideScreen)
                }

                scanDetailState.isLoading -> {
                    CircularProgressIndicator()
                }

                scanDetailState.polygons.isNotEmpty() -> {
                    BathymetryMap(
                        polygons = scanDetailState.polygons.toWrapperList(),
                        bbox = scanDetailState.bbox,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                scanDetailState.errorMessage != null -> {
                    Text(
                        text = scanDetailState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    val scan = selectedScan
                    if (scan != null) {
                        DetailPlaceholder(
                            scan = scan,
                            isWideScreen = isWideScreen,
                            onNavigateClick = { onNavigateClick(scan.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptySelectionView(isWideScreen: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.List,
            contentDescription = null,
            modifier = Modifier.height(if (isWideScreen) 96.dp else 64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.select_scan_hint),
            style = if (isWideScreen) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DetailPlaceholder(
    scan: ScanUiModel,
    isWideScreen: Boolean,
    onNavigateClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isWideScreen) 32.dp else 24.dp)
        ) {
            Text(
                text = scan.name,
                style = if (isWideScreen) MaterialTheme.typography.headlineLarge else MaterialTheme.typography.headlineMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(if (isWideScreen) 12.dp else 8.dp))
            Text(
                text = scan.date,
                style = if (isWideScreen) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(if (isWideScreen) 24.dp else 16.dp))

            // Stats card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(if (isWideScreen) 24.dp else 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${scan.scanPoints}",
                        style = if (isWideScreen) MaterialTheme.typography.displayLarge else MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = stringResource(Res.string.scan_points),
                        style = if (isWideScreen) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Navigate button
            Card(
                onClick = onNavigateClick,
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(if (isWideScreen) 24.dp else 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.view_bathymetry_map),
                        style = if (isWideScreen) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(Res.string.open_full_screen),
                        style = if (isWideScreen) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
internal fun ScanItem(
    scan: ScanUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = scan.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = scan.date,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(Res.string.scan_points_format, scan.scanPoints),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun EmptyScansView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ) {
        Text(
            text = stringResource(Res.string.scan_empty_title),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.scan_empty_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
