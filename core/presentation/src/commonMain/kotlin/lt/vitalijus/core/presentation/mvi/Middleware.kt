package lt.vitalijus.core.presentation.mvi

/**
 * Middleware for handling side effects in MVI pattern.
 *
 * Implement this interface to handle async operations (API calls, navigation, etc.)
 * that should occur after state updates.
 *
 * @param I Intent type
 * @param S State type
 * @param E Effect type
 */
fun interface Middleware<I : UiIntent, S : UiState, E : UiEffect> {
    /**
     * Handles side effects for the given intent and state.
     *
     * @param intent The intent that triggered this middleware
     * @param state The current state after reducer processed the intent
     * @param dispatchIntent Function to dispatch new intents (e.g., for error handling)
     * @param emitEffect Function to emit one-time effects (e.g., navigation, toasts)
     */
    suspend fun handle(
        intent: I,
        state: S,
        dispatchIntent: suspend (I) -> Unit,
        emitEffect: suspend (E) -> Unit
    )
}
