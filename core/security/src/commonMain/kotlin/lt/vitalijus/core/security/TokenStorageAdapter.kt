package lt.vitalijus.core.security

import lt.vitalijus.core.domain.util.Result

/**
 * Adapter that wraps SecureStorage to implement TokenStorage interface.
 */
class TokenStorageAdapter(
    private val secureStorage: SecureStorage
) : TokenStorage {

    override suspend fun saveToken(token: String): Result<Unit, StorageError> {
        return try {
            secureStorage.saveToken(token)
            Result.Success(Unit)
        } catch (e: SecureStorageException) {
            Result.Failure(StorageError.IOError(e))
        }
    }

    override suspend fun getToken(): Result<String, StorageError> {
        return try {
            val token = secureStorage.getToken()
            if (token != null) {
                Result.Success(token)
            } else {
                Result.Failure(StorageError.NotFound)
            }
        } catch (e: SecureStorageException) {
            Result.Failure(StorageError.IOError(e))
        }
    }

    override suspend fun clearToken(): Result<Unit, StorageError> {
        return try {
            secureStorage.clearToken()
            Result.Success(Unit)
        } catch (e: SecureStorageException) {
            Result.Failure(StorageError.IOError(e))
        }
    }

    override suspend fun hasToken(): Result<Boolean, StorageError> {
        return try {
            Result.Success(secureStorage.hasToken())
        } catch (e: SecureStorageException) {
            Result.Failure(StorageError.IOError(e))
        }
    }
}
