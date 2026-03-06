package lt.vitalijus.core.database.mappers

import kotlinx.serialization.json.Json
import lt.vitalijus.core.database.entity.BathymetryEntity
import lt.vitalijus.core.database.entity.BathymetryJson
import lt.vitalijus.core.domain.model.BathymetryData

/**
 * Serializes BathymetryData to JSON string for database storage.
 */
fun BathymetryData.toJson(): String {
    val jsonModel = this.toJsonModel()
    return Json.encodeToString(jsonModel)
}

/**
 * Deserializes JSON string to BathymetryData from database storage.
 */
fun String.toBathymetryData(): BathymetryData {
    val jsonModel = Json.decodeFromString<BathymetryJson>(this)
    return jsonModel.toDomain()
}

/**
 * Creates BathymetryEntity from scanId and BathymetryData.
 */
fun BathymetryData.toEntity(scanId: Long): BathymetryEntity {
    return BathymetryEntity(
        scanId = scanId,
        jsonData = this.toJson()
    )
}
