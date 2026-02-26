package lt.vitalijus.feature.scan.presentation.scans

import lt.vitalijus.core.presentation.mvi.MviViewModel

/**
 * ViewModel for scan list screen.
 */
class ScanListViewModel(
    middleware: ScanListMiddleware
) : MviViewModel<ScanListIntent, ScanListState, ScanListEffect>(
    initialState = ScanListState(),
    reducer = createScanListReducer(),
    middleware = middleware
)
