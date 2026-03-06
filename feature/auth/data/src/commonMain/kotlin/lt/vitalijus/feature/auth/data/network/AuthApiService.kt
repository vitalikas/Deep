package lt.vitalijus.feature.auth.data.network

import io.ktor.client.HttpClient
import lt.vitalijus.core.data.networking.post
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result

class AuthApiService(
    private val httpClient: HttpClient
) {

    companion object {
        private const val LOGIN_ENDPOINT = "/api/login"
    }

    suspend fun login(
        email: String,
        password: String
    ): Result<LoginResponseDto, DataError.Remote> {
        val request = LoginRequestDto(
            email = email,
            password = password
        )

        return httpClient.post(
            route = LOGIN_ENDPOINT,
            body = request
        )
    }
}
