package lt.vitalijus.feature.scan.presentation.scandetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import kotlinx.cinterop.memScoped
import lt.vitalijus.feature.scan.presentation.model.PolygonWrapper
import lt.vitalijus.feature.scan.presentation.util.DepthColor
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation

/**
 * iOS implementation of BathymetryMap using MapKit with colored pins.
 *
 * Renders bathymetry data with colored pins based on depth.
 * Full polygon rendering requires Swift implementation.
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun BathymetryMap(
    polygons: List<PolygonWrapper>,
    bbox: List<Double>,
    modifier: Modifier
) {
    val centerLat = if (bbox.size >= 4) {
        (bbox[0] + bbox[2]) / 2
    } else {
        polygons.firstOrNull()?.polygon?.geometry?.coordinates?.firstOrNull()?.firstOrNull()
            ?.getOrNull(1)
            ?: 55.0
    }
    val centerLon = if (bbox.size >= 4) {
        (bbox[1] + bbox[3]) / 2
    } else {
        polygons.firstOrNull()?.polygon?.geometry?.coordinates?.firstOrNull()?.firstOrNull()
            ?.getOrNull(0)
            ?: 23.0
    }

    // Create annotations with depth-based colors
    val annotationsWithColors = remember(polygons) {
        polygons.mapNotNull { wrapper ->
            val polygon = wrapper.polygon
            val firstRing = polygon.geometry.coordinates.firstOrNull()
            val firstCoord = firstRing?.firstOrNull()
            if (firstCoord != null && firstCoord.size >= 2) {
                val annotation = MKPointAnnotation().apply {
                    setCoordinate(
                        CLLocationCoordinate2DMake(
                            firstCoord[1], // lat
                            firstCoord[0]  // lon
                        )
                    )
                    setTitle("${polygon.depth}m")
                    setSubtitle("Bathymetry")
                }
                val color = DepthColor.fromDepth(polygon.depth)
                Pair(annotation, color.hexColor)
            } else null
        }
    }

    androidx.compose.ui.viewinterop.UIKitView(
        modifier = modifier.fillMaxSize(),
        factory = {
            MKMapView().apply {
                memScoped {
                    val region = MKCoordinateRegionMakeWithDistance(
                        cValue {
                            latitude = centerLat
                            longitude = centerLon
                        },
                        2000.0,
                        2000.0
                    )
                    setRegion(region, animated = false)
                }

                // Add all annotations
                annotationsWithColors.forEach { (annotation, _) ->
                    addAnnotation(annotation)
                }
            }
        },
        update = { mapView ->
            memScoped {
                val region = MKCoordinateRegionMakeWithDistance(
                    cValue {
                        latitude = centerLat
                        longitude = centerLon
                    },
                    2000.0,
                    2000.0
                )
                mapView.setRegion(region, animated = true)
            }
        }
    )
}
