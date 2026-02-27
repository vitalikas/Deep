package lt.vitalijus.feature.scan.presentation.scans

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Unit tests for ScanListReducer.
 */
class ScanListReducerTest {

    private val reducer = createScanListReducer()

    @Test
    fun `LoadScans intent sets loading true and clears error`() {
        // Given
        val initialState = ScanListState(
            scans = emptyList(),
            isLoading = false,
            errorMessage = "Previous error"
        )

        // When
        val newState = reducer.reduce(
            state = initialState,
            intent = ScanListIntent.LoadScans
        )

        // Then
        assertTrue(newState.isLoading)
        assertNull(newState.errorMessage)
    }

    @Test
    fun `OnScansLoaded intent sets scans and loading false`() {
        // Given
        val initialState = ScanListState(
            scans = emptyList(),
            isLoading = true,
            errorMessage = null
        )
        val loadedScans = listOf(
            ScanUiModel(
                id = 1,
                name = "Test Scan",
                date = "2024-01-15",
                location = "54.0, 25.0",
                scanPoints = 100
            )
        )

        // When
        val newState = reducer.reduce(
            state = initialState,
            intent = ScanListIntent.OnScansLoaded(loadedScans)
        )

        // Then
        assertFalse(newState.isLoading)
        assertEquals(loadedScans, newState.scans)
        assertEquals(1, newState.scans.size)
    }

    @Test
    fun `OnError intent sets error message and loading false`() {
        // Given
        val initialState = ScanListState(
            scans = emptyList(),
            isLoading = true,
            errorMessage = null
        )
        val errorMessage = "Network error"

        // When
        val newState = reducer.reduce(
            state = initialState,
            intent = ScanListIntent.OnError(errorMessage)
        )

        // Then
        assertFalse(newState.isLoading)
        assertEquals(errorMessage, newState.errorMessage)
    }

    @Test
    fun `OnScanClick intent does not change state`() {
        // Given
        val initialState = ScanListState(
            scans = listOf(
                ScanUiModel(
                    id = 1,
                    name = "Test",
                    date = "2024-01-15",
                    location = "54.0, 25.0",
                    scanPoints = 100
                )
            ),
            isLoading = false,
            errorMessage = null
        )

        // When
        val newState = reducer.reduce(
            state = initialState, intent = ScanListIntent.OnScanClick(1L)
        )

        // Then
        assertEquals(initialState, newState)
    }

    @Test
    fun `OnRefresh intent sets loading true and clears error`() {
        // Given
        val initialState = ScanListState(
            scans = emptyList(),
            isLoading = false,
            errorMessage = "Previous error"
        )

        // When
        val newState = reducer.reduce(
            state = initialState,
            intent = ScanListIntent.OnRefresh
        )

        // Then
        assertTrue(newState.isLoading)
        assertNull(newState.errorMessage)
    }

    @Test
    fun `initial state has empty scans and not loading`() {
        // Given
        val initialState = ScanListState()

        // Then
        assertTrue(initialState.scans.isEmpty())
        assertFalse(initialState.isLoading)
        assertNull(initialState.errorMessage)
    }
}
