package lt.vitalijus.feature.scan.presentation.scandetail

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

/**
 * Root composable for Scan Detail screen.
 *
 * @param scanId The scan ID to display
 * @param scanName The scan name for title
 * @param viewModel The ViewModel - injected by Koin (shared instance)
 * @param onNavigateBack Callback when user performs some action (provides scanId)
 */
@Composable
fun ScanDetailScreenRoot(
    scanId: Long,
    scanName: String,
    viewModel: ScanDetailViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    ScanDetailScreen(
        scanId = scanId,
        scanName = scanName,
        viewModel = viewModel,
        onNavigateBack = onNavigateBack
    )
}
