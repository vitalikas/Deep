package lt.vitalijus.feature.scan.presentation.scandetail

import lt.vitalijus.core.presentation.mvi.MviViewModel

/**
 * ViewModel for scan detail screen.
 */
class ScanDetailViewModel(
    middleware: ScanDetailMiddleware
) : MviViewModel<ScanDetailIntent, ScanDetailState, ScanDetailEffect>(
    initialState = ScanDetailState(),
    reducer = createScanDetailReducer(),
    middleware = middleware
)
