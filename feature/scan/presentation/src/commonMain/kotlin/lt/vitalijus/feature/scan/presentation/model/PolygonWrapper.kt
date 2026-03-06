package lt.vitalijus.feature.scan.presentation.model

import androidx.compose.runtime.Immutable
import lt.vitalijus.core.domain.model.Polygon

/**
 * Immutable wrapper for Polygon to ensure Compose stability.
 *
 * The underlying Polygon data class is immutable by design (all vals),
 * but Compose compiler cannot guarantee this across module boundaries.
 * This wrapper explicitly marks the data as @Immutable for Compose optimization.
 */
@Immutable
data class PolygonWrapper(
    val polygon: Polygon
)

/**
 * Extension to wrap a list of Polygons for stable Compose parameters.
 */
fun List<Polygon>.toWrapperList(): List<PolygonWrapper> = map { PolygonWrapper(it) }
