package lt.vitalijus.core.database.entity

import kotlinx.serialization.Serializable

/**
 * Serializable JSON representation of BathymetryData for Room database storage.
 * This is NOT a DTO - it's an internal serialization format for storing
 * complex GeoJSON data as a JSON string in the database.
 */
@Serializable
internal data class BathymetryJson(
    val type: String,
    val bbox: List<Double>,
    val features: List<PolygonJson>
)

/**
 * JSON representation of Polygon for database storage.
 */
@Serializable
internal data class PolygonJson(
    val id: String,
    val depth: Double,
    val geometry: PolygonGeometryJson
)

/**
 * JSON representation of PolygonGeometry for database storage.
 */
@Serializable
internal data class PolygonGeometryJson(
    val type: String,
    val bbox: List<Double>,
    val coordinates: List<List<List<Double>>> // GeoJSON Polygon: [ring[coord[lon,lat,depth]]]
)
