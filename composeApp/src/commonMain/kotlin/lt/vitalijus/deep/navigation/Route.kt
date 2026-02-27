package lt.vitalijus.deep.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Login : Route

    @Serializable
    data object ScanList : Route

    @Serializable
    data class ScanDetail(val scanId: Long, val scanName: String) : Route
}
