package lt.vitalijus.feature.scan.presentation.scandetail

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Root composable for Scan Detail screen.
 *
 * @param scanId The scan ID to display
 * @param scanName The scan name for title
 * @param viewModel The ViewModel - injected by Koin
 * @param onNavigateBack Callback when user navigates back
 */
@Composable
fun ScanDetailScreenRoot(
    scanId: Long,
    scanName: String,
    viewModel: ScanDetailViewModel = koinViewModel { parametersOf(scanId, scanName) },
    onNavigateBack: () -> Unit
) {
    ScanDetailScreen(
        scanId = scanId,
        scanName = scanName,
        viewModel = viewModel,
        onNavigateBack = onNavigateBack
    )
}
