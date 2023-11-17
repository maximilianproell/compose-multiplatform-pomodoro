package com.compose.multiplatform.pomodoro.ui.components.chart

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import at.maximilianproell.multiplatformchart.barchart.BarChart
import at.maximilianproell.multiplatformchart.barchart.model.BarChartEntry
import com.compose.multiplatform.pomodoro.domain.usecase.GetBarChartDataUseCase

@Composable
fun WeeklyBarchart(
    modifier: Modifier = Modifier,
    weeksFromCurrentWeek: Int,
) {
    val screenState = rememberWeeklyBarChartState(weeksFromCurrentWeek)

    BarChart(
        modifier = modifier.fillMaxSize(),
        entries = screenState.barChartEntries.value,
        maxYValue = maxOf(1f, screenState.barChartEntries.value.maxOfOrNull { it.yValue } ?: 0f),
    )
}

@Stable
class WeeklyBarChartState(
    private val weeksFromCurrentWeek: Int,
) {
    private val getBarChartDataUseCase: GetBarChartDataUseCase = GetBarChartDataUseCase()

    val barChartEntries: State<List<BarChartEntry>>
        @Composable get() = produceState(initialValue = emptyList()) {
            value = getBarChartDataUseCase(weeksFromCurrentWeek)
        }
}

@Composable
private fun rememberWeeklyBarChartState(
    weeksFromCurrentWeek: Int
) = remember(weeksFromCurrentWeek) {
    WeeklyBarChartState(weeksFromCurrentWeek)
}
