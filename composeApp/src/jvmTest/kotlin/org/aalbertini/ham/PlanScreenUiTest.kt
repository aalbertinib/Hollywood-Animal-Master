package org.aalbertini.ham

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.v2.runDesktopComposeUiTest
import org.aalbertini.ham.core.util.format.grouped
import org.aalbertini.ham.features.movie_distribution.domain.calculator.DistributionPlanner
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionMethod
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.movie_distribution.presentation.PlannerUiState
import org.aalbertini.ham.features.movie_distribution.presentation.screen.PlanScreen
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Drives the real Plan screen. The window is tall enough for the lazy lists to compose every week.
 * Texts are the English resources.
 */
@OptIn(ExperimentalTestApi::class)
class PlanScreenUiTest {

    private val movie = MovieResult(
        id = "",
        title = "Test",
        commercialScore = 5.0,
        numberOfScreenings = 3200.0,
        method = DistributionMethod.FAST_DECAY,
    )

    private fun state(movie: MovieResult) = PlannerUiState(movie, DistributionPlanner.plan(movie), emptyList())

    /** Label of the per-week own-screenings field: one per open week. */
    private val weekEditor = "Own screenings this week"

    @Test
    fun clickingTheWeekRow_opensAndClosesIt() = runDesktopComposeUiTest(width = 1280, height = 4000) {
        setContent { MaterialTheme { PlanScreen(state(movie), onEdit = {}) } }
        onAllNodesWithText(weekEditor).assertCountEquals(0)

        onNodeWithText("Week 2").performClick()
        onAllNodesWithText(weekEditor).assertCountEquals(1)

        onNodeWithText("Week 2").performClick()
        onAllNodesWithText(weekEditor).assertCountEquals(0)
    }

    @Test
    fun expandAll_opensOnlyThatGroup_andTurnsIntoCollapseAll() = runDesktopComposeUiTest(width = 1280, height = 4000) {
        setContent { MaterialTheme { PlanScreen(state(movie), onEdit = {}) } }
        // Two groups (weeks 1–4 and 5–8): the first "Expand all" is the initial run.
        onAllNodesWithText("Expand all").assertCountEquals(2)
        onAllNodesWithText("Expand all")[0].performClick()
        onAllNodesWithText(weekEditor).assertCountEquals(4)
        onAllNodesWithText("Collapse all").assertCountEquals(1)

        onNodeWithText("Collapse all").performClick()
        onAllNodesWithText(weekEditor).assertCountEquals(0)
    }

    @Test
    fun openingEveryWeekByHand_switchesTheGroupButton() = runDesktopComposeUiTest(width = 1280, height = 4000) {
        setContent { MaterialTheme { PlanScreen(state(movie), onEdit = {}) } }
        (1..4).forEach { onNodeWithText("Week $it").performClick() }
        onAllNodesWithText("Collapse all").assertCountEquals(1)
    }

    @Test
    fun typingAScore_replansTheSummary() = runDesktopComposeUiTest(width = 1280, height = 4000) {
        var current by mutableStateOf(state(movie))
        setContent {
            MaterialTheme {
                PlanScreen(current, onEdit = { transform -> current = state(transform(current.movie)) })
            }
        }
        // The KPI is read out as one sentence, so match its accessible description.
        onNodeWithContentDescription("To rent: ${current.plan.totalRent.grouped()} screenings").assertExists()

        // "," is accepted as the decimal separator, as typed on a French keyboard.
        onNodeWithText("Commercial score").performTextReplacement("6,5")
        waitForIdle()
        assertEquals(6.5, current.movie.commercialScore)
        val after = DistributionPlanner.plan(movie.copy(commercialScore = 6.5)).totalRent.grouped()
        onNodeWithContentDescription("To rent: $after screenings").assertExists()
    }

    @Test
    fun outOfRangeScore_showsAnError_andKeepsTheLastValidValue() = runDesktopComposeUiTest(width = 1280, height = 4000) {
        var current by mutableStateOf(state(movie))
        setContent {
            MaterialTheme {
                PlanScreen(current, onEdit = { transform -> current = state(transform(current.movie)) })
            }
        }
        onNodeWithText("Commercial score").performTextReplacement("42")
        waitForIdle()
        onNodeWithText("Between 0.1 and 10").assertExists()
        assertEquals(5.0, current.movie.commercialScore)
    }
}
