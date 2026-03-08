package lt.vitalijus.feature.auth.domain

import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.EmptyResult
import lt.vitalijus.core.domain.util.Result

/**
 * Repository for authentication operations.
 */
interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResult, DataError>
    suspend fun logout(): EmptyResult<DataError.Local>
}
