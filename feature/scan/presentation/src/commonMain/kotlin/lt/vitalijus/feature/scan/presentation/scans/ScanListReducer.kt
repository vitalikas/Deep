package lt.vitalijus.feature.scan.presentation.scans

import lt.vitalijus.core.presentation.mvi.Reducer
import lt.vitalijus.core.presentation.mvi.reducer

/**
 * Pure reducer for Scan list feature - no side effects, just state transformations.
 */
internal fun createScanListReducer(): Reducer<ScanListState, ScanListIntent> = reducer {
    on<ScanListIntent.LoadScans> { state, _ ->
        state.copy(isLoading = true, errorMessage = null)
    }
    on<ScanListIntent.OnScansLoaded> { state, intent ->
        state.copy(
            isLoading = false,
            scans = intent.scans,
            errorMessage = null
        )
    }
    on<ScanListIntent.OnError> { state, intent ->
        state.copy(
            isLoading = false,
            errorMessage = intent.message
        )
    }
    on<ScanListIntent.OnScanClick> { state, _ ->
        state
    }
    on<ScanListIntent.OnRefresh> { state, _ ->
        state.copy(isLoading = true, errorMessage = null)
    }
}
