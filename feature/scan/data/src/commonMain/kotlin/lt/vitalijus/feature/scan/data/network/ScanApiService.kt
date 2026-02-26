package lt.vitalijus.feature.scan.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.scan.domain.BathymetryData
import lt.vitalijus.feature.scan.domain.Polygon
import lt.vitalijus.feature.scan.domain.PolygonGeometry

class ScanApiService(
    private val httpClient: HttpClient
) {
    companion object {
        private const val BASE_URL = "https://bathus.staging.deeper.eu"
        private const val GEODATA_ENDPOINT = "/api/geoData"
    }

    suspend fun getBathymetry(scanId: Long, token: String): Result<BathymetryData, DataError.Remote> {
        return try {
            val response = httpClient.get("$BASE_URL$GEODATA_ENDPOINT") {
                parameter("grid", "FAST")
                parameter("generator", "BS")
                parameter("scanIds", scanId.toString())
                parameter("token", token)
            }

            if (response.status.isSuccess()) {
                val jsonText = response.bodyAsText()
                val bathymetry = parseBathymetryJson(jsonText)
                Result.Success(bathymetry)
            } else {
                Result.Failure(
                    when (response.status.value) {
                        401 -> DataError.Remote.UNAUTHORIZED
                        408 -> DataError.Remote.REQUEST_TIMEOUT
                        else -> DataError.Remote.SERVER_ERROR
                    }
                )
            }
        } catch (e: Exception) {
            Result.Failure(DataError.Remote.NO_INTERNET)
        }
    }

    private fun parseBathymetryJson(jsonText: String): BathymetryData {
        val json = Json.parseToJsonElement(jsonText).jsonObject
        val bathymetryObj = json["bathymetry"]?.jsonObject
            ?: return BathymetryData("FeatureCollection", emptyList(), emptyList())

        val bbox = bathymetryObj["bbox"]?.jsonArray?.map {
            it.jsonPrimitive.content.toDoubleOrNull() ?: 0.0
        } ?: emptyList()

        val features = bathymetryObj["features"]?.jsonArray?.map { featureEl ->
            val feature = featureEl.jsonObject
            val properties = feature["properties"]?.jsonObject
            val geometry = feature["geometry"]?.jsonObject

            val id = properties?.get("id")?.jsonPrimitive?.content ?: ""
            val depth = properties?.get("depth")?.jsonPrimitive?.content?.toDoubleOrNull() ?: 0.0

            val geomBbox = geometry?.get("bbox")?.jsonArray?.map {
                it.jsonPrimitive.content.toDoubleOrNull() ?: 0.0
            } ?: emptyList()

            val coordinates = geometry?.get("coordinates")?.jsonArray?.map { ringEl ->
                ringEl.jsonArray.map { coordEl ->
                    coordEl.jsonArray.map {
                        it.jsonPrimitive.content.toDoubleOrNull() ?: 0.0
                    }
                }
            } ?: emptyList()

            Polygon(
                id = id,
                depth = depth,
                geometry = PolygonGeometry(
                    type = "Polygon",
                    bbox = geomBbox,
                    coordinates = coordinates
                )
            )
        } ?: emptyList()

        return BathymetryData(
            type = "FeatureCollection",
            bbox = bbox,
            features = features
        )
    }
}
