package lt.vitalijus.core.presentation.mvi

import kotlin.reflect.KClass

/**
 * Reducer function interface.
 */
fun interface Reducer<S : UiState, I : UiIntent> {
    fun reduce(state: S, intent: I): S
}

/**
 * Creates a reducer using DSL syntax.
 *
 * This provides a type-safe way to handle different intents without explicit
 * "when" expressions. Each `on<T>` block registers a handler for that specific
 * intent type.
 *
 * ## Example
 * ```kotlin
 * private fun createReducer(): Reducer<LoginState, LoginIntent> = reducer {
 *     on<LoginIntent.OnEmailChange> { state, intent ->
 *         state.copy(email = intent.email, errorMessage = null)
 *     }
 *     on<LoginIntent.OnPasswordChange> { state, intent ->
 *         state.copy(password = intent.password, errorMessage = null)
 *     }
 *     on<LoginIntent.OnLoginClick> { state, _ ->
 *         state.copy(isLoading = true, errorMessage = null)
 *     }
 *     on<LoginIntent.OnLoginError> { state, intent ->
 *         state.copy(isLoading = false, errorMessage = intent.message)
 *     }
 * }
 * ```
 *
 * ## How it works
 * 1. Each `on<T>()` registers a handler for intent type T
 * 2. At runtime, the builder finds the first matching handler using `isInstance`
 * 3. If no handler matches, returns the state unchanged
 */
fun <S : UiState, I : UiIntent> reducer(
    builder: ReducerBuilder<S, I>.() -> Unit
): Reducer<S, I> {
    return ReducerBuilder<S, I>() // Creating an empty builder
        .apply(builder) // Calling a lambda with the builder as a receiver
        .build() // Building the reducer
}

/**
 * DSL builder for reducers with type-safe handling.
 */
class ReducerBuilder<S : UiState, I : UiIntent> {

    // Saves as Pair -> (IntentKlass, HandlerFunctionLambda(State, Intent))
    val handlers = mutableListOf<Pair<KClass<out I>, (S, I) -> S>>()

    inline fun <reified T : I> on(crossinline block: (S, T) -> S) {
        // reified = we know the class T at compile-time
        // crossinline = lambda cannot return early
        @Suppress("UNCHECKED_CAST")
        handlers.add(
            T::class as KClass<out I> to { state: S, intent: I -> // Pair<KClass, Lambda)
                block(state, intent as T) // cast T, calling a block
            }
        )
    }

    fun build(): Reducer<S, I> = Reducer { state, intent ->
        // Find the handler by intent class
        handlers.find { (kclass, _) -> kclass.isInstance(intent) }
            ?.second?.invoke(state, intent)
            ?: state
    }
}
