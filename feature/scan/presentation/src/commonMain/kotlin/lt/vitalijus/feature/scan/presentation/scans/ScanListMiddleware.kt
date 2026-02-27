package lt.vitalijus.feature.scan.presentation.scans

import lt.vitalijus.core.domain.util.onFailure
import lt.vitalijus.core.domain.util.onSuccess
import lt.vitalijus.core.presentation.mvi.Middleware
import lt.vitalijus.feature.auth.domain.usecases.LogoutUseCase
import lt.vitalijus.feature.scan.domain.usecase.ClearAllCacheUseCase
import lt.vitalijus.feature.scan.domain.usecase.GetScansUseCase

/**
 * Middleware for handling scan list side effects.
 */
class ScanListMiddleware(
    private val getScansUseCase: GetScansUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val clearAllCacheUseCase: ClearAllCacheUseCase
) : Middleware<ScanListIntent, ScanListState, ScanListEffect> {

    override suspend fun handle(
        intent: ScanListIntent,
        state: ScanListState,
        dispatchIntent: suspend (ScanListIntent) -> Unit,
        emitEffect: suspend (ScanListEffect) -> Unit
    ) {
        when (intent) {
            is ScanListIntent.LoadScans, is ScanListIntent.OnRefresh -> {
                loadScans(
                    dispatchIntent = dispatchIntent,
                    emitEffect = emitEffect
                )
            }

            is ScanListIntent.OnScanClick -> {
                emitEffect(ScanListEffect.NavigateToScanDetail(scanId = intent.scanId))
            }

            is ScanListIntent.OnLogoutClick -> {
                logout(
                    dispatchIntent = dispatchIntent,
                    emitEffect = emitEffect
                )
            }

            else -> {} // OnScansLoaded, OnError, OnLoggedOut are handled by reducer only
        }
    }

    private suspend fun loadScans(
        dispatchIntent: suspend (ScanListIntent) -> Unit,
        emitEffect: suspend (ScanListEffect) -> Unit
    ) {
        getScansUseCase()
            .onSuccess { scans ->
                dispatchIntent(ScanListIntent.OnScansLoaded(scans = scans.toUiModels()))
            }
            .onFailure { error ->
                dispatchIntent(ScanListIntent.OnError(message = "Failed to load scans: $error"))
                emitEffect(ScanListEffect.ShowToast(message = "Failed to load scans"))
            }
    }

    private suspend fun logout(
        dispatchIntent: suspend (ScanListIntent) -> Unit,
        emitEffect: suspend (ScanListEffect) -> Unit
    ) {
        // First clear all cached data for security
        clearAllCacheUseCase()

        logoutUseCase()
            .onSuccess {
                dispatchIntent(ScanListIntent.OnLoggedOut)
                emitEffect(ScanListEffect.NavigateToLogin)
            }
            .onFailure { error ->
                dispatchIntent(ScanListIntent.OnError(message = "Logout failed: $error"))
                emitEffect(ScanListEffect.ShowToast(message = "Logout failed"))
            }
    }
}
