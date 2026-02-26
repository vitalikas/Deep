package lt.vitalijus.feature.scan.presentation.scans

import lt.vitalijus.feature.scan.domain.model.Scan

/**
 * Maps Scan domain model to ScanUiModel for UI presentation.
 */
fun Scan.toUiModel(): ScanUiModel = ScanUiModel(
    id = id,
    name = name ?: "Scan #$id",
    date = date ?: "Unknown date",
    location = "${lat}, $lon",
    scanPoints = scanPoints
)

/**
 * Maps list of Scan domain models to list of ScanUiModels.
 */
fun List<Scan>.toUiModels(): List<ScanUiModel> = map { it.toUiModel() }
