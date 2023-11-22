package com.compose.multiplatform.pomodoro.ui.components.chart

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import at.maximilianproell.multiplatformchart.barchart.BarChart
import com.compose.multiplatform.pomodoro.MR
import com.compose.multiplatform.pomodoro.domain.model.BarChartData
import com.compose.multiplatform.pomodoro.domain.usecase.GetBarChartDataUseCase
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlin.math.ceil

@Composable
fun WeeklyBarchart(
    modifier: Modifier = Modifier,
    weeksFromCurrentWeek: Int,
) {
    val screenState = rememberWeeklyBarChartState(weeksFromCurrentWeek)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Crossfade(
            modifier = Modifier.fillMaxWidth(),
            targetState = screenState.chartTitle
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = it,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(32.dp))

        val barChartData by screenState.barChartData

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            BarChart(
                modifier = Modifier.fillMaxSize(),
                entries = barChartData?.barChartEntries ?: emptyList(),
                maxYValue = maxOf(1f, barChartData?.barChartEntries?.maxOfOrNull { ceil(it.yValue) } ?: 0f),
            )
            if (barChartData == null) {
                CircularProgressIndicator()
            }
        }

    }
}

@Stable
class WeeklyBarChartState(
    private val weeksFromCurrentWeek: Int,
) {
    private val getBarChartDataUseCase: GetBarChartDataUseCase = GetBarChartDataUseCase()

    val barChartData: State<BarChartData?>
        @Composable get() = produceState<BarChartData?>(initialValue = null) {
            value = getBarChartDataUseCase(weeksFromCurrentWeek)
        }

    val chartTitle: String
        @Composable get() {
            return when (val tmpBarChartData = barChartData.value) {
                null -> "- – -"
                else -> {
                    if (weeksFromCurrentWeek == 0) {
                        stringResource(MR.strings.bar_chart_title_this_week)
                    } else {
                        val from = tmpBarChartData.fromDate
                        val to = tmpBarChartData.toDate
                        "${from.formatForTitle()} – ${to.formatForTitle()}"
                    }
                }
            }
        }

}

@Composable
private fun rememberWeeklyBarChartState(
    weeksFromCurrentWeek: Int
) = remember(weeksFromCurrentWeek) {
    WeeklyBarChartState(weeksFromCurrentWeek)
}

/**
 * Formats this [Month] in such a way, that the first letter is capitalized and the
 * name is abbreviated to 3 letters (e.g., November becomes Nov).
 */
private fun Month.formatForTitle(): String = toString()
    .lowercase()
    .take(3)
    .capitalize(Locale.current)

/**
 * Formats this [LocalDate] into the format Mon. D, YYYY (e.g., Nov. 8, 2023)
 */
private fun LocalDate.formatForTitle(): String = "${month.formatForTitle()}. $dayOfMonth, $year"