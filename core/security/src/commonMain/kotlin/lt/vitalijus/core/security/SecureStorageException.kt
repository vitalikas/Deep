package lt.vitalijus.core.security

/**
 * Exception thrown when secure storage operations fail.
 */
class SecureStorageException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
