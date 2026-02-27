package lt.vitalijus.feature.scan.data.mappers

import kotlinx.serialization.json.Json
import lt.vitalijus.core.database.entity.BathymetryEntity
import lt.vitalijus.core.database.entity.ScanEntity
import lt.vitalijus.feature.scan.domain.model.BathymetryData
import lt.vitalijus.feature.scan.domain.model.Scan

/**
 * Maps ScanEntity to Scan domain model.
 */
fun ScanEntity.toDomain(): Scan {
    return Scan(
        id = id,
        lat = lat,
        lon = lon,
        name = name,
        date = date,
        scanPoints = scanPoints,
        mode = mode
    )
}

/**
 * Maps Scan domain model to ScanEntity.
 */
fun Scan.toEntity(): ScanEntity {
    return ScanEntity(
        id = id,
        lat = lat,
        lon = lon,
        name = name,
        date = date,
        scanPoints = scanPoints,
        mode = mode
    )
}

/**
 * Maps List<Scan> to List<ScanEntity>.
 */
fun List<Scan>.toEntities(): List<ScanEntity> = map { it.toEntity() }

/**
 * Maps BathymetryData to JSON string.
 */
fun BathymetryData.toJson(): String = Json.encodeToString(this)

/**
 * Maps JSON string to BathymetryData.
 */
fun String.toBathymetryData(): BathymetryData = Json.decodeFromString(this)

/**
 * Creates BathymetryEntity from scanId and BathymetryData.
 */
fun BathymetryData.toEntity(scanId: Long): BathymetryEntity {
    return BathymetryEntity(
        scanId = scanId,
        jsonData = this.toJson()
    )
}
