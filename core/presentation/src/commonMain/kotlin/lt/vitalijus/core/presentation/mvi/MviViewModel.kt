package lt.vitalijus.core.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel with integrated MVI store.
 *
 * Uses [viewModelScope] for coroutine lifecycle management.
 */
abstract class MviViewModel<I : UiIntent, S : UiState, E : UiEffect>(
    initialState: S,
    reducer: Reducer<S, I>,
    middleware: Middleware<I, S, E> = Middleware { _, _, _, _ -> }
) : ViewModel() {

    protected val store = object : BaseStore<I, S, E>(
        initialState = initialState,
        reducer = reducer,
        middleware = middleware,
        scope = viewModelScope
    ) {

    }

    val state: StateFlow<S> = store.state
    val currentState: S get() = store.currentState
    val effect: SharedFlow<E> = store.effect

    fun dispatch(intent: I) = store.dispatch(intent = intent)
}
