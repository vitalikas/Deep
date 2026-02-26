package lt.vitalijus.feature.scan.domain.model

/**
 * Bathymetry data model containing GeoJSON feature collection.
 */
data class BathymetryData(
    val type: String,
    val bbox: List<Double>,
    val features: List<Polygon>
)

/**
 * Single bathymetry feature (polygon) with depth info.
 */
data class Polygon(
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
