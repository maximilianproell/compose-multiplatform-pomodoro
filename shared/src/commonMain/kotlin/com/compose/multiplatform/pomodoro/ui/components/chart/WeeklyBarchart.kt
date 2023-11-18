package com.compose.multiplatform.pomodoro.ui.components.chart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import at.maximilianproell.multiplatformchart.barchart.BarChart
import com.compose.multiplatform.pomodoro.domain.model.BarChartData
import com.compose.multiplatform.pomodoro.domain.usecase.GetBarChartDataUseCase
import com.compose.multiplatform.pomodoro.utils.Result

@Composable
fun WeeklyBarchart(
    modifier: Modifier = Modifier,
    weeksFromCurrentWeek: Int,
) {
    val screenState = rememberWeeklyBarChartState(weeksFromCurrentWeek)
    val barChartData = screenState.barChartData.value

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = screenState.chartTitle, style = MaterialTheme.typography.headlineMedium)

        when (barChartData) {
            is Result.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is Result.Success -> {
                BarChart(
                    modifier = Modifier.fillMaxSize(),
                    entries = barChartData.data.barChartEntries,
                    maxYValue = maxOf(1f, barChartData.data.barChartEntries.maxOfOrNull { it.yValue } ?: 0f),
                )
            }
        }
    }

}

@Stable
class WeeklyBarChartState(
    private val weeksFromCurrentWeek: Int,
) {
    private val getBarChartDataUseCase: GetBarChartDataUseCase = GetBarChartDataUseCase()

    val barChartData: State<Result<BarChartData>>
        @Composable get() = produceState<Result<BarChartData>>(initialValue = Result.Loading) {
            value = Result.Success(getBarChartDataUseCase(weeksFromCurrentWeek))
        }

    val chartTitle: String
        @Composable get() {
            return when (val tmpBarChartData = barChartData.value) {
                is Result.Loading -> "- - -"
                is Result.Success -> {
                    val data by tmpBarChartData
                    val from = data.fromDate
                    val to = data.toDate
                    "$from - $to"
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
