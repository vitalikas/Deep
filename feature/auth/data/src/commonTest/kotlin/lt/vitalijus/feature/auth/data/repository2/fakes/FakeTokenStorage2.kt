package lt.vitalijus.feature.auth.data.repository2.fakes

import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.core.security.StorageError
import lt.vitalijus.core.security.TokenStorage

class FakeTokenStorage2(
    private val saveSuccess: Boolean = true
) : TokenStorage {

    var savedToken: String? = null
        private set

    var wasCleared: Boolean = false
        private set

    override suspend fun hasToken(): Result<Boolean, StorageError> {
        return Result.Success(savedToken != null)
    }

    override suspend fun saveToken(token: String): Result<Unit, StorageError> {
        return if (saveSuccess) {
            savedToken = token
            Result.Success(Unit)
        } else {
            Result.Failure(StorageError.Unknown(Exception("Save failed")))
        }
    }

    override suspend fun getToken(): Result<String, StorageError> {
        return savedToken?.let { Result.Success(it) }
            ?: Result.Failure(StorageError.NotFound)
    }

    override suspend fun clearToken(): Result<Unit, StorageError> {
        savedToken = null
        wasCleared = true
        return Result.Success(Unit)
    }
}
