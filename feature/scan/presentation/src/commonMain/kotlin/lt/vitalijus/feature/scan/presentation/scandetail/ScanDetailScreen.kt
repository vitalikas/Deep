package lt.vitalijus.feature.scan.presentation.scandetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lt.vitalijus.feature.scan.domain.Polygon

/**
 * Scan detail screen with bathymetry map.
 *
 * @param scanId The scan ID to display
 * @param scanName The scan name for title
 * @param viewModel The ViewModel
 * @param onNavigateBack Callback when user wants to go back
 */
@Composable
fun ScanDetailScreen(
    scanId: Long,
    scanName: String,
    viewModel: ScanDetailViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Handle effects (navigation, toasts)
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ScanDetailEffect.NavigateBack -> onNavigateBack()
                is ScanDetailEffect.ShowToast -> {
                    // Show toast - platform specific or using snackbar
                }
            }
        }
    }

    // Load scan data on first composition (not on rotation if already loaded)
    LaunchedEffect(scanId) {
        if (state.polygons.isEmpty() && !state.isLoading) {
            viewModel.dispatch(ScanDetailIntent.LoadScan(scanId, scanName))
        }
    }

    ScanDetailContent(
        state = state,
        onIntent = viewModel::dispatch,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScanDetailContent(
    state: ScanDetailState,
    onIntent: (ScanDetailIntent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.scanName.ifBlank { "Scan #${state.scanId}" },
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(ScanDetailIntent.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.errorMessage != null -> {
                    Text(
                        text = state.errorMessage,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                state.polygons.isNotEmpty() -> {
                    BathymetryMap(
                        features = state.polygons,
                        bbox = state.bbox,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    Text(
                        text = "No bathymetry data available",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

/**
 * Platform-specific bathymetry map composable.
 *
 * Implementations:
 * - Android: Uses Google Maps Compose with polygons
 * - iOS: Uses MapKit or Google Maps iOS SDK
 *
 * @param features List of bathymetry features (polygons) to render
 * @param bbox Bounding box [minLat, minLon, maxLat, maxLon]
 * @param modifier Modifier for the composable
 */
@Composable
expect fun BathymetryMap(
    features: List<Polygon>,
    bbox: List<Double>,
    modifier: Modifier = Modifier
)
