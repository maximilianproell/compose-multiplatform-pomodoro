package com.compose.multiplatform.pomodoro.ui.screens.statistics

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.compose.multiplatform.pomodoro.domain.repository.WorkPackageRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.ceil

class StatisticsScreenModel :
    StateScreenModel<StatisticsScreenModel.StatisticsScreenState>(StatisticsScreenState()),
    KoinComponent {

    private val workPackageRepository: WorkPackageRepository by inject()

    data class StatisticsScreenState(
        val numberOfPages: Int = 1,
    )

    init {
        coroutineScope.launch {
            val workPackages = workPackageRepository.observeAllWorkPackages().first()
            val oldestDate = workPackages.firstOrNull()?.endDate

            if (oldestDate != null) {
                val instantPast = oldestDate.toInstant(TimeZone.currentSystemDefault())
                val todayInstant = Clock.System.now()
                val todayDate = todayInstant.toLocalDateTime(TimeZone.currentSystemDefault())
                val daysPassed = instantPast.until(todayInstant, DateTimeUnit.DAY, TimeZone.currentSystemDefault())

                // Week must have passed, therefore add extra week.
                val additionalWeek = if (oldestDate.dayOfWeek.ordinal >= todayDate.dayOfWeek.ordinal) 1 else 0
                val numberOfPages = ceil(daysPassed / 7f).toInt() + additionalWeek

                mutableState.update {
                    it.copy(numberOfPages = numberOfPages)
                }
            }
        }
    }
}