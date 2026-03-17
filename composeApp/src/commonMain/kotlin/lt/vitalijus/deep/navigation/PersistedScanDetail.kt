package lt.vitalijus.deep.navigation

import kotlinx.serialization.Serializable

@Serializable
data class PersistedScanDetail(
    val scanId: Long,
    val scanName: String
)
