package lt.vitalijus.feature.scan.domain

/**
 * Domain model for a scan (batimetric record).
 */
data class Scan(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val name: String?,
    val date: String?,
    val scanPoints: Int,
    val mode: Int
)
