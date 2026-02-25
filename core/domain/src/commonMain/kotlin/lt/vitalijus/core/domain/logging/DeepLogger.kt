package lt.vitalijus.core.domain.logging

interface DeepLogger {
    fun debug(message: String)
    fun warn(message: String)
    fun error(message: String, throwable: Throwable? = null)
}
