package lt.vitalijus.feature.auth.data.repository2.fakes

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.auth.data.network.LoginDataDto
import lt.vitalijus.feature.auth.data.network.LoginResponseDto
import lt.vitalijus.feature.auth.data.network.ScanDto
import lt.vitalijus.feature.auth.data.network.UserDto
import lt.vitalijus.feature.auth.data.network2.AuthApiService2

class FakeAuthApiService2(
    private val success: Boolean = true
) : AuthApiService2 {

    override suspend fun login(
        email: String,
        password: String
    ): Result<LoginResponseDto, DataError.Remote> {
        return if (success) {
            Result.Success(
                LoginResponseDto(
                    login = LoginDataDto(
                        token = "fake-token-123",
                        userId = 1L,
                        validated = true,
                        validTill = "2026-12-31",
                        registrationDate = "2025-01-01"
                    ),
                    user = UserDto(
                        id = 1L,
                        email = email,
                        name = "Test User"
                    ),
                    scans = listOf(
                        ScanDto(
                            id = 1L,
                            lat = 54.0,
                            lon = 25.0,
                            scanPoints = 100,
                            mode = 1
                        )
                    )
                )
            )
        } else {
            Result.Failure(DataError.Remote.UNAUTHORIZED)
        }
    }
}
