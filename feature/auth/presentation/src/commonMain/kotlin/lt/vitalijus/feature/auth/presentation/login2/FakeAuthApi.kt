package lt.vitalijus.feature.auth.presentation.login2

import kotlinx.coroutines.delay
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.EmptyResult
import lt.vitalijus.core.domain.util.Result

class FakeAuthApi(
    private val success: Boolean
) {

    suspend fun login(
        email: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        // simulate network call
        delay(1000)
        return if (success) {
            Result.Success(Unit)
        } else {
            Result.Failure(DataError.Remote.UNAUTHORIZED)
        }
    }
}
