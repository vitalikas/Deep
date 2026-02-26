package lt.vitalijus.feature.scan.presentation.scandetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import lt.vitalijus.feature.scan.domain.Polygon

/**
 * iOS implementation of BathymetryMap using MapKit.
 *
 * TODO: Implement using MapKit with UIViewControllerRepresentable
 */
@Composable
actual fun BathymetryMap(
    features: List<Polygon>,
    bbox: List<Double>,
    modifier: Modifier
) {
    // TODO: Implement iOS map using MapKit
    // For now, show placeholder
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Map view - iOS implementation needed")
    }
}
