package com.compose.multiplatform.pomodoro.domain.usecase

import at.maximilianproell.multiplatformchart.barchart.model.BarChartEntry
import com.compose.multiplatform.pomodoro.domain.model.BarChartData
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

class GetBarChartDataUseCase : KoinComponent {

    private val logger = createLogger()
    private val workPackageRepository: WorkPackageRepository by inject()

    /**
     * Gets the chart data with the work data from the week with the given offset.
     * @param weekOffsetFromCurrentWeek The offset from the current week, where an offset of 0 is the current week.
     */
    suspend operator fun invoke(weekOffsetFromCurrentWeek: Int): BarChartData {
        val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val daysUntilSunday = daysUntilSundayFrom(todayDate)
        val sundayThisWeek = todayDate.plus(daysUntilSunday, DateTimeUnit.DAY)
        val mondayThisWeek = sundayThisWeek.minus(6, DateTimeUnit.DAY)

        val monday = mondayThisWeek.minus(weekOffsetFromCurrentWeek * 7, DateTimeUnit.DAY)
        val sunday = sundayThisWeek.minus(weekOffsetFromCurrentWeek * 7, DateTimeUnit.DAY)

        logger.d { "Getting data for week $monday - $sunday" }

        // TODO: get correct range directly from DB for better performance.
        val workPackages = workPackageRepository
            .observeAllWorkPackages()
            .first()

        val barChartEntries = buildList {
            DayOfWeek.values().forEach { dayOfWeek ->
                val weekDayDate = monday.plus(dayOfWeek.ordinal, DateTimeUnit.DAY)
                val minutesWorked = workPackages
                    // Filter for this specific day.
                    .filter { it.endDate.date == weekDayDate }
                    .sumOf { it.minutes }

                // TODO: localization of text.
                add(BarChartEntry(dayOfWeek.name.take(2), minutesWorked / 60f))
            }

        }

        return BarChartData(
            fromDate = monday,
            toDate = sunday,
            barChartEntries = barChartEntries,
        )
    }

    private fun daysUntilSundayFrom(localDate: LocalDate): Int =
        DayOfWeek.SUNDAY.ordinal - localDate.dayOfWeek.ordinal
}
