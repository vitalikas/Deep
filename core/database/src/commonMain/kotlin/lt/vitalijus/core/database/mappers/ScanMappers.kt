package lt.vitalijus.core.database.mappers

import lt.vitalijus.core.database.entity.ScanEntity
import lt.vitalijus.core.domain.model.Scan

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
