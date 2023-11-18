package com.compose.multiplatform.pomodoro.domain.model

import at.maximilianproell.multiplatformchart.barchart.model.BarChartEntry
import kotlinx.datetime.LocalDate

data class BarChartData(
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val barChartEntries: List<BarChartEntry>,
)
