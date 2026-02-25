package lt.vitalijus.core.domain.util

sealed interface Result<out D, out E : Error> {

    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Failure<out E : Error>(val error: E) : Result<Nothing, E>
}

/**
 * Transforms the success value of this Result using the given transform function.
 *
 * If this is a Success, applies the transform function to the data and returns a new Success
 * with the transformed value. If this is a Failure, returns the Failure unchanged.
 *
 * @param T The type of the current success data
 * @param E The type of error that extends Error
 * @param R The type of the transformed success data
 * @param transform The function to apply to the success data
 * @return A new Result with transformed data if Success, or the original Failure
 *
 * Example:
 * ```kotlin
 * // Transform DTO to Domain model
 * val dtoResult: Result<List<ChatDto>, DataError.Remote> = fetchChatsFromApi()
 * val domainResult: Result<List<ChatDomain>, DataError.Remote> = dtoResult.map { dtos ->
 *     dtos.map { it.toDomain() }
 * }
 * ```
 */
inline fun <T, E : Error, R> Result<T, E>.map(transform: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Failure -> Result.Failure(error = this.error)
        is Result.Success -> Result.Success(data = transform(this.data))
    }
}

/**
 * Executes the given action if this Result is a Success.
 *
 * @param T The type of success data
 * @param E The type of error that extends Error
 * @param action The action to perform with the success data
 * @return This Result unchanged for chaining
 *
 * Example:
 * ```kotlin
 * fun loadChats(): Result<List<String>, DataError.Remote> {
 *     return Result.Success(data = listOf("Chat 1", "Chat 2", "Chat 3"))
 * }
 *
 * fun fetchChats() {
 *     loadChats().onSuccess { chats ->
 *         // do something with chats
 *     }
 * }
 * ```
 */
inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Failure -> this
        is Result.Success -> {
            action(this.data)
            this
        }
    }
}

/**
 * Executes the given action if this Result is a Failure.
 *
 * @param T The type of success data
 * @param E The type of error that extends Error
 * @param action The action to perform with the failure data
 * @return This Result unchanged for chaining
 *
 * Example:
 * ```kotlin
 * fun loadChats(): Result<List<String>, DataError.Remote> {
 *     return Result.Failure(error = DataError.Remote("Failed to load chats"))
 * }
 *
 * fun fetchChats() {
 *     loadChats().onFailure { error ->
 *         // handle the error
 *     }
 * }
 * ```
 */
inline fun <T, E : Error> Result<T, E>.onFailure(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Failure -> {
            action(this.error)
            this
        }

        is Result.Success -> this
    }
}

/**
 * A Result type that carries no success data, only success/failure status.
 *
 * Useful when you only care about whether an operation succeeded or failed,
 * not about any return value.
 */
typealias EmptyResult<E> = Result<Unit, E>

/**
 * Converts this Result to an EmptyResult, discarding any success data.
 *
 * If this is a Success, the data is replaced with Unit. If this is a Failure,
 * the error is preserved unchanged. This is useful when you want to ignore
 * the success value and only care about success/failure status.
 *
 * @param T The type of the current success data (will be discarded)
 * @param E The type of error that extends Error
 * @return An EmptyResult that indicates success or failure without data
 *
 * Example:
 * ```kotlin
 * // Delete operation returns the deleted item, but we don't need it
 * fun deleteChat(id: String): Result<Chat, DataError.Remote> = chatApi.delete(id)
 *
 * // Convert to EmptyResult since we only care if deletion succeeded
 * suspend fun deleteChatUseCase(id: String): EmptyResult<DataError.Remote> {
 *     return deleteChat(id).withEmptyResult()
 * }
 *
 * // Usage
 * deleteChatUseCase("123").onSuccess {
 *     println("Chat deleted successfully")
 * }
 * ```
 */
fun <T, E : Error> Result<T, E>.withEmptyResult(): EmptyResult<E> {
    return this.map(transform = { _ -> Unit })
}
