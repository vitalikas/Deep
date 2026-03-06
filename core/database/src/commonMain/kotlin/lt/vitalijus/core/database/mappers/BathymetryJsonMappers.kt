package lt.vitalijus.core.database.mappers

import lt.vitalijus.core.database.entity.BathymetryJson
import lt.vitalijus.core.database.entity.PolygonGeometryJson
import lt.vitalijus.core.database.entity.PolygonJson
import lt.vitalijus.core.domain.model.BathymetryData
import lt.vitalijus.core.domain.model.Polygon
import lt.vitalijus.core.domain.model.PolygonGeometry

/**
 * Extension functions for converting between Bathymetry JSON and Domain models.
 */

/**
 * Convert JSON to Domain model.
 */
internal fun BathymetryJson.toDomain(): BathymetryData {
    return BathymetryData(
        type = this.type,
        bbox = this.bbox,
        polygons = this.features.map { it.toDomain() }
    )
}

/**
 * Convert Domain model to JSON.
 */
internal fun BathymetryData.toJsonModel(): BathymetryJson {
    return BathymetryJson(
        type = this.type,
        bbox = this.bbox,
        features = this.polygons.map { it.toJsonModel() }
    )
}

private fun PolygonJson.toDomain(): Polygon {
    return Polygon(
        id = this.id,
        depth = this.depth,
        geometry = this.geometry.toDomain()
    )
}

private fun Polygon.toJsonModel(): PolygonJson {
    return PolygonJson(
        id = this.id,
        depth = this.depth,
        geometry = this.geometry.toJsonModel()
    )
}

private fun PolygonGeometryJson.toDomain(): PolygonGeometry {
    return PolygonGeometry(
        type = this.type,
        bbox = this.bbox,
        coordinates = this.coordinates
    )
}

private fun PolygonGeometry.toJsonModel(): PolygonGeometryJson {
    return PolygonGeometryJson(
        type = this.type,
        bbox = this.bbox,
        coordinates = this.coordinates
    )
}
