package lt.vitalijus.feature.auth.domain.usecases2

import kotlinx.coroutines.test.runTest
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LoginUseCase2Test {

    @Test
    fun `invalid email returns INVALID_EMAIL error`() = runTest {
        val useCase = LoginUseCase2(authRepository2 = FakeAuthRepository2())

        val result = useCase(
            email = "testtest.com",
            password = "pass123"
        )

        assertIs<Result.Failure<DataError>>(result)
        assertEquals(DataError.Validation.INVALID_EMAIL, result.error)
    }

    @Test
    fun `email with spaces gets trimmed`() = runTest {
        val useCase = LoginUseCase2(authRepository2 = FakeAuthRepository2())

        val result = useCase(
            email = "  test@test.com  ",
            password = "pass123"
        )

        assertIs<Result.Success<Unit>>(result)
    }

    @Test
    fun `repository failure propagates error`() = runTest {
        val failingRepo = FakeAuthRepository2(
            result = Result.Failure(DataError.Remote.UNAUTHORIZED)
        )
        val useCase = LoginUseCase2(authRepository2 = failingRepo)

        val result = useCase(
            email = "test@test.com",
            password = "wrong"
        )

        assertIs<Result.Failure<DataError>>(result)
        assertEquals(DataError.Remote.UNAUTHORIZED, result.error)
    }
}
