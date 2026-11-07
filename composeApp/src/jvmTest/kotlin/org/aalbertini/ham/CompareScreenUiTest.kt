package org.aalbertini.ham

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.v2.runDesktopComposeUiTest
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionMethod
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.movie_distribution.presentation.screen.CompareScreen
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class CompareScreenUiTest {

    private val movies = listOf(
        MovieResult("a", "Fast", 6.5, 3200.0, method = DistributionMethod.FAST_DECAY),
        MovieResult("b", "Steam", 6.5, 3200.0, method = DistributionMethod.STEAM_GUIDE),
        MovieResult("c", "Long", 7.0, 2000.0, method = DistributionMethod.TWO_THIRDS, runWeeks = 12),
    )

    private val horizontal = SemanticsMatcher.keyIsDefined(SemanticsProperties.HorizontalScrollAxisRange)

    @Test
    fun phoneWidth_allRowsOfBothTablesScrollTogether_labelsStay() = runDesktopComposeUiTest(width = 375, height = 2000) {
        setContent { MaterialTheme { CompareScreen(movies, onOpen = {}) } }

        // Three 112 dp columns don't fit beside the labels at phone width.
        val before = onAllNodes(horizontal).fetchSemanticsNodes()
        assertTrue(before.first().config[SemanticsProperties.HorizontalScrollAxisRange].maxValue() > 0f)

        onAllNodes(horizontal and hasAnyDescendant(hasText("Week 1").not()) and hasAnyDescendant(hasText("9,800")))[0]
            .performSemanticsAction(SemanticsActions.ScrollBy) { it(80f, 0f) }
        waitForIdle()

        val offsets = onAllNodes(horizontal).fetchSemanticsNodes()
            .map { it.config[SemanticsProperties.HorizontalScrollAxisRange].value() }
        assertEquals(setOf(80f), offsets.toSet(), "every row of both tables at the same offset")
        // Week labels are outside the scrolling part, so they are all still there.
        assertEquals(12, (1..12).count { onAllNodesWithText("Week $it").fetchSemanticsNodes().isNotEmpty() })
    }

    @Test
    fun wideWindow_columnsFit_nothingScrolls() = runDesktopComposeUiTest(width = 1100, height = 2000) {
        setContent { MaterialTheme { CompareScreen(movies, onOpen = {}) } }
        val maxes = onAllNodes(horizontal).fetchSemanticsNodes()
            .map { it.config[SemanticsProperties.HorizontalScrollAxisRange].maxValue() }
        assertEquals(setOf(0f), maxes.toSet())
    }
}
