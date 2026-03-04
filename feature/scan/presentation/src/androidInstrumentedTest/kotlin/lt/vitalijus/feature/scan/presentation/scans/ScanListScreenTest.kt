package lt.vitalijus.feature.scan.presentation.scans

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import lt.vitalijus.feature.scan.presentation.scandetail.ScanDetailState
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented UI tests for ScanListScreen.
 * These tests require an Android emulator or device.
 */
class ScanListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun scanList_displaysItemsAndClick() {
        // Given
        val fakeScans = listOf(
            ScanUiModel(
                id = 1,
                name = "Scan A",
                date = "2024-01-15",
                location = "54.0, 25.0",
                scanPoints = 100
            ),
            ScanUiModel(
                id = 2,
                name = "Scan B",
                date = "2024-01-16",
                location = "54.1, 25.1",
                scanPoints = 200
            )
        )

        var clickedScanId: Long? = null

        // When
        composeTestRule.setContent {
            ScanListScreen(
                scanListState = ScanListState(
                    scans = fakeScans,
                    isLoading = false,
                    errorMessage = null
                ),
                scanDetailState = ScanDetailState(),
                onScanListIntent = { intent ->
                    if (intent is ScanListIntent.OnScanClick) {
                        clickedScanId = intent.scanId
                    }
                },
                onScanDetailIntent = {}
            )
        }

        // Then -> scans should be displayed
        composeTestRule
            .onNodeWithText("Scan A")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Scan B")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Scan A")
            .performClick()

        // Then -> click should trigger intent
        assert(clickedScanId == 1L)
    }

    @Test
    fun scanList_showsLoadingState() {
        // When
        composeTestRule.setContent {
            ScanListScreen(
                scanListState = ScanListState(
                    scans = emptyList(),
                    isLoading = true,
                    errorMessage = null
                ),
                scanDetailState = ScanDetailState(),
                onScanListIntent = {},
                onScanDetailIntent = {}
            )
        }

        // Then -> loading indicator should be visible
        composeTestRule
            .onNodeWithTag("loadingIndicator")
            .assertIsDisplayed()
    }

    @Test
    fun scanList_showsEmptyState() {
        // When
        composeTestRule.setContent {
            ScanListScreen(
                scanListState = ScanListState(
                    scans = emptyList(),
                    isLoading = false,
                    errorMessage = null
                ),
                scanDetailState = ScanDetailState(),
                onScanListIntent = {},
                onScanDetailIntent = {}
            )
        }

        // Then
        composeTestRule
            .onNodeWithText("No scans yet")
            .assertIsDisplayed()
    }
}
