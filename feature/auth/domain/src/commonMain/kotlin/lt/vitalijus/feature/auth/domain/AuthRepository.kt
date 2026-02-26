package lt.vitalijus.feature.auth.domain

import kotlinx.coroutines.flow.Flow
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.EmptyResult
import lt.vitalijus.core.domain.util.Result

interface AuthRepository {
    val currentUser: Flow<User?>
    val isAuthenticated: Flow<Boolean>

    suspend fun login(email: String, password: String): Result<LoginResult, DataError>
    suspend fun logout(): EmptyResult<DataError.Local>
    suspend fun getCurrentToken(): String?
    suspend fun isTokenValid(): Boolean
}
