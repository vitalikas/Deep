package lt.vitalijus.core.security

import lt.vitalijus.core.domain.util.Error

/**
 * Errors that can occur during token storage operations.
 */
sealed interface StorageError : Error {
    data object NotFound : StorageError
    data class EncryptionFailed(val cause: Throwable) : StorageError
    data class DecryptionFailed(val cause: Throwable) : StorageError
    data class IOError(val cause: Throwable) : StorageError
    data class Unknown(val cause: Throwable) : StorageError
}
