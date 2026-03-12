package lt.vitalijus.feature.auth.data.network2

import io.ktor.client.HttpClient
import lt.vitalijus.core.data.networking.post
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.auth.data.network.LoginRequestDto
import lt.vitalijus.feature.auth.data.network.LoginResponseDto

interface AuthApiService2 {
    suspend fun login(email: String, password: String): Result<LoginResponseDto, DataError.Remote>
}

class AuthApiService2Impl(
    private val httpClient: HttpClient
) : AuthApiService2 {

    companion object {
        private const val LOGIN_ENDPOINT = "/api/login"
    }

    override suspend fun login(
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
