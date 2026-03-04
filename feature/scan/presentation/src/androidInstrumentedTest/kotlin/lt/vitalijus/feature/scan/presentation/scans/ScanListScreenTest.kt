package lt.vitalijus.feature.scan.presentation.scans

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import lt.vitalijus.feature.scan.presentation.scandetail.ScanDetailState
import org.junit.Assert.assertEquals
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
    fun scanItem_clickTriggersCallback() {
        // Given
        val scan = ScanUiModel(
            id = 1,
            name = "Test Scan",
            date = "2024-01-15",
            location = "54.0, 25.0",
            scanPoints = 100
        )
        var clicked = false

        // When - test ScanItem directly
        composeTestRule.setContent {
            ScanItem(
                scan = scan,
                onClick = { clicked = true }
            )
        }

        composeTestRule.waitForIdle()

        // Then
        composeTestRule
            .onNode(hasText("Test Scan") and hasClickAction())
            .assertIsDisplayed()

        composeTestRule
            .onNode(hasText("Test Scan") and hasClickAction())
            .performClick()

        composeTestRule.waitForIdle()

        // Then
        assertEquals(true, clicked)
    }

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
                    when (intent) {
                        is ScanListIntent.OnScanClick -> clickedScanId = intent.scanId
                        is ScanListIntent.OnSelectScan -> clickedScanId = intent.scanId
                        else -> {}
                    }
                },
                onScanDetailIntent = {}
            )
        }

        composeTestRule.waitForIdle()

        // Then -> scans should be displayed
        composeTestRule
            .onNodeWithText("Scan A")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Scan B")
            .assertIsDisplayed()

        composeTestRule
            .onNode(hasText("Scan A") and hasClickAction())
            .performClick()

        composeTestRule.waitForIdle()

        // Then -> click should trigger intent
        assertEquals(1L, clickedScanId)
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
