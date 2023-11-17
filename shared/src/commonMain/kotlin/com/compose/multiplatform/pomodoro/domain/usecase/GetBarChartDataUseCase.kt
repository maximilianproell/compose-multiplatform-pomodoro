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
        val mondayThisWeek = sundayThisWeek.minus(6, DateTimeUnit.DAY)

        logger.d { "Today is: $todayDate" }
        logger.d { "Sunday this week: $sundayThisWeek" }
        logger.d { "Monday this week: $mondayThisWeek" }

        val workPackages = workPackageRepository
            .observeAllWorkPackages()
            .first()
            .filter { it.endDate.date >= mondayThisWeek }

        return buildList {
            DayOfWeek.values().forEach { dayOfWeek ->
                val weekDayDate = mondayThisWeek.plus(dayOfWeek.ordinal, DateTimeUnit.DAY)
                val minutesWorked = workPackages
                    // Filter for this specific day.
                    .filter { it.endDate.date == weekDayDate }
                    .sumOf { it.minutes }

                // TODO: localization of text.
                add(BarChartEntry(dayOfWeek.name.take(2), minutesWorked / 60f))
            }

        }
    }

    private fun daysUntilSundayFrom(localDate: LocalDate): Int =
        DayOfWeek.SUNDAY.ordinal - localDate.dayOfWeek.ordinal
}
