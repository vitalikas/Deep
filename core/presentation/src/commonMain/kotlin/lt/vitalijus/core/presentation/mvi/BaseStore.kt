package lt.vitalijus.core.presentation.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Base store implementing MVI+Redux pattern with partial state support.
 */
abstract class BaseStore<I : UiIntent, S : UiState, E : UiEffect>(
    initialState: S,
    private val reducer: Reducer<S, I>,
    private val middleware: Middleware<I, S, E> = Middleware { _, _, _, _ -> },
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<E>()
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

        scope.launch {
            middleware.handle(
                intent = intent,
                state = newState,
                dispatchIntent = ::dispatch,
                emitEffect = _effect::emit
            )
        }
    }
}
