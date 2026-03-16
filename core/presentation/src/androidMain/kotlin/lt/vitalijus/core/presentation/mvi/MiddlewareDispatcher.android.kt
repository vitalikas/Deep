package lt.vitalijus.core.presentation.mvi

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual val middlewareDispatcher: CoroutineDispatcher = Dispatchers.Default
