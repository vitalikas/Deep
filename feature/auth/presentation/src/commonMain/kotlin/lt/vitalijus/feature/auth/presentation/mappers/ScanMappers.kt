package lt.vitalijus.feature.auth.presentation.mappers

import lt.vitalijus.feature.auth.domain.ScanInfo
import lt.vitalijus.feature.scan.domain.model.Scan

/**
 * Maps ScanInfo from auth domain to Scan model for scan module.
 */
fun ScanInfo.toScan(): Scan = Scan(
    id = id,
    lat = lat,
    lon = lon,
    name = name,
    date = date,
    scanPoints = scanPoints,
    mode = mode
)

/**
 * Maps list of ScanInfo to list of Scan.
 */
fun List<ScanInfo>.toScans(): List<Scan> = map { it.toScan() }
