package com.compose.multiplatform.pomodoro.ui.screens.statistics

import at.maximilianproell.multiplatformchart.barchart.model.BarChartEntry
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.compose.multiplatform.pomodoro.domain.usecase.GetBarChartDataUseCase
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class StatisticsScreenModel :
    StateScreenModel<StatisticsScreenModel.StatisticsScreenState>(StatisticsScreenState()),
    KoinComponent {

    private val getBarChartDataUseCase: GetBarChartDataUseCase = GetBarChartDataUseCase()

    data class StatisticsScreenState(
        val isLoading: Boolean = false,
        val barChartEntries: List<BarChartEntry> = emptyList(),
    )

    init {
        mutableState.update { it.copy(isLoading = true) }
        coroutineScope.launch {
            val barChartData = getBarChartDataUseCase()
            mutableState.update {
                it.copy(barChartEntries = barChartData)
            }
        }
    }
}