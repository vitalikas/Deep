@file:OptIn(ExperimentalAtomicApi::class)

package lt.vitalijus.core.presentation.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

/**
 * Base store implementing MVI+Redux pattern with partial state support.
 */
abstract class BaseStore<I : UiIntent, S : UiState, E : UiEffect>(
    initialIntent: I? = null,
    initialState: S,
    private val reducer: Reducer<S, I>,
    private val middleware: Middleware<I, S, E> = Middleware { _, _, _, _ -> },
    private val scope: CoroutineScope,
) {

    private var didDispatchInitial = AtomicBoolean(false)
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state
        .onStart {
            // Dispatch initial intent once (init is thread-safe and runs before external access)
            if (initialIntent != null && didDispatchInitial.compareAndSet(
                    expectedValue = false,
                    newValue = true
                )
            ) {
                dispatch(intent = initialIntent)
            }
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
            initialValue = initialState
        )

    private val _effect = MutableSharedFlow<E>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<E> = _effect.asSharedFlow()

    /**
     * Current state value (for synchronous reads).
     */
    val currentState: S get() = _state.value

    /**
     * Dispatches an intent to update state via reducer and trigger middleware.
     */
    fun dispatch(intent: I) {
        val newState = reducer.reduce(_state.value, intent)
        _state.value = newState

        scope.launch(middlewareDispatcher) {
            middleware.handle(
                intent = intent,
                state = newState,
                dispatchIntent = ::dispatch,
                emitEffect = _effect::emit
            )
        }
    }
}
