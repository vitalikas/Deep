package lt.vitalijus.feature.scan.presentation.scandetail

import lt.vitalijus.core.presentation.mvi.Reducer
import lt.vitalijus.core.presentation.mvi.reducer

/**
 * Pure reducer for Scan detail feature - no side effects, just state transformations.
 */
internal fun createScanDetailReducer(): Reducer<ScanDetailState, ScanDetailIntent> = reducer {
    on<ScanDetailIntent.LoadScan> { state, intent ->
        state.copy(
            scanId = intent.scanId,
            scanName = intent.scanName,
            isLoading = true,
            errorMessage = null
        )
    }
    on<ScanDetailIntent.OnBathymetryLoaded> { state, intent ->
        state.copy(
            isLoading = false,
            features = intent.features,
            bbox = intent.bbox,
            errorMessage = null
        )
    }
    on<ScanDetailIntent.OnError> { state, intent ->
        state.copy(
            isLoading = false,
            errorMessage = intent.message
        )
    }
    on<ScanDetailIntent.OnBackClick> { state, _ ->
        state
    }
}
