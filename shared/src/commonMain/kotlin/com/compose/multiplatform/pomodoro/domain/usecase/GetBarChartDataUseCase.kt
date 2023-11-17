package com.compose.multiplatform.pomodoro.domain.usecase

import at.maximilianproell.multiplatformchart.barchart.model.BarChartEntry
import com.compose.multiplatform.pomodoro.domain.repository.WorkPackageRepository
import com.compose.multiplatform.pomodoro.utils.createLogger
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Gets the chart data with the work data from the current week.
 */
class GetBarChartDataUseCase : KoinComponent {

    private val logger = createLogger()
    private val workPackageRepository: WorkPackageRepository by inject()

    suspend operator fun invoke(): List<BarChartEntry> {
        val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val daysUntilSunday = daysUntilSundayFrom(todayDate)
        val sundayThisWeek = todayDate.plus(daysUntilSunday, DateTimeUnit.DAY)
        val mondayThisWeek = sundayThisWeek.minus(7, DateTimeUnit.DAY)

        logger.d { "Today is: $todayDate" }
        logger.d { "Sunday this week: $sundayThisWeek" }

        val workPackages = workPackageRepository
            .observeAllWorkPackages()
            .first()
            .filter { it.endDate.date >= mondayThisWeek }

        return buildList {
            // 7 days per week
            DayOfWeek.values().forEachIndexed { dayOfWeekIndex, dayOfWeek ->
                val minutesWorked = workPackages
                    .filter { it.endDate.date == mondayThisWeek.plus(dayOfWeekIndex, DateTimeUnit.DAY) }
                    .sumOf { it.minutes }

                add(BarChartEntry(dayOfWeek.name.take(2).uppercase(), minutesWorked / 60f))
            }

        }
    }

    private fun daysUntilSundayFrom(localDate: LocalDate): Int =
        DayOfWeek.SUNDAY.ordinal - localDate.dayOfWeek.ordinal
}
