package lt.vitalijus.feature.scan.presentation.scandetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import lt.vitalijus.feature.scan.domain.BathymetryFeature
import lt.vitalijus.feature.scan.domain.getDepthColor
import androidx.core.graphics.toColorInt

/**
 * Android implementation of BathymetryMap using Google Maps Compose.
 *
 * Renders bathymetry polygons with depth-based coloring.
 */
@Composable
actual fun BathymetryMap(
    features: List<BathymetryFeature>,
    bbox: List<Double>,
    modifier: Modifier
) {
    // Calculate center from bbox [minLat, minLon, maxLat, maxLon]
    val centerLat = if (bbox.size >= 4) {
        (bbox[0] + bbox[2]) / 2
    } else {
        features.firstOrNull()?.geometry?.coordinates?.firstOrNull()?.firstOrNull()?.get(1) ?: 0.0
    }
    val centerLon = if (bbox.size >= 4) {
        (bbox[1] + bbox[3]) / 2
    } else {
        features.firstOrNull()?.geometry?.coordinates?.firstOrNull()?.firstOrNull()?.get(0) ?: 0.0
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(centerLat, centerLon),
            15f
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        features.forEach { feature ->
            val color = getDepthColor(feature.depth)
            // Convert GeoJSON coordinates to LatLng list
            // GeoJSON: [lon, lat, depth] -> Google Maps: LatLng(lat, lon)
            val points = feature.geometry.coordinates.firstOrNull()?.map { coord ->
                LatLng(
                    coord.getOrElse(1) { 0.0 }, // lat
                    coord.getOrElse(0) { 0.0 }  // lon
                )
            } ?: emptyList()

            if (points.isNotEmpty()) {
                // Parse hex color to Compose Color
                val fillColorInt = color.hexColor.toColorInt()
                val fillColor = Color(fillColorInt)
                val strokeColor = Color.Black

                Polygon(
                    points = points,
                    fillColor = fillColor,
                    strokeColor = strokeColor,
                    strokeWidth = 2f
                )
            }
        }
    }
}
