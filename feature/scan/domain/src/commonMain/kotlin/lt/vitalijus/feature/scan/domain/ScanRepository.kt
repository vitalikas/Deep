package lt.vitalijus.feature.scan.domain

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result

/**
 * Repository for scan-related data operations.
 */
interface ScanRepository {
    /**
     * Get bathymetry GeoJSON data for a specific scan.
     *
     * @param scanId The scan ID
     * @return Result containing bathymetry data or error
     */
    suspend fun getBathymetry(scanId: Long): Result<Bathymetry, DataError>
}

/**
 * Bathymetry data model containing GeoJSON feature collection.
 */
data class Bathymetry(
    val type: String,
    val bbox: List<Double>,
    val features: List<BathymetryFeature>
)

/**
 * Single bathymetry feature (polygon) with depth info.
 */
data class BathymetryFeature(
    val id: String,
    val depth: Double,
    val geometry: PolygonGeometry
)

/**
 * Polygon geometry with coordinates.
 */
data class PolygonGeometry(
    val type: String,
    val bbox: List<Double>,
    val coordinates: List<List<List<Double>>> // GeoJSON Polygon: [ring[coord[lon,lat,depth]]]
)

/**
 * Depth range with associated color for visualization.
 */
data class DepthRange(
    val minDepth: Double,
    val maxDepth: Double,
    val color: DepthColor
)

/**
 * Predefined depth colors for bathymetry visualization.
 */
enum class DepthColor(val hexColor: String) {
    SHALLOW("#FF0000"),    // 0-1m: Red
    MEDIUM("#00FF00"),     // 1-2m: Green
    DEEP("#0000FF"),       // 3-4m: Blue
    VERY_DEEP("#800080")   // >4m: Purple
}

/**
 * Get color for a given depth value.
 */
fun getDepthColor(depth: Double): DepthColor {
    return when {
        depth <= 1.0 -> DepthColor.SHALLOW
        depth <= 2.0 -> DepthColor.MEDIUM
        depth <= 4.0 -> DepthColor.DEEP
        else -> DepthColor.VERY_DEEP
    }
}
