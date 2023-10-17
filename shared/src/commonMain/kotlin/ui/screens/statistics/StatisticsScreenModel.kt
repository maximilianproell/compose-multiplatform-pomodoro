package ui.screens.statistics

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import domain.model.WorkPackage
import domain.repository.WorkPackageRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class StatisticsScreenModel :
    StateScreenModel<StatisticsScreenModel.StatisticsScreenState>(StatisticsScreenState()),
    KoinComponent {

    private val workPackageRepository: WorkPackageRepository = get()

    data class StatisticsScreenState(
        // TODO: Just for testing right now
        val isLoading: Boolean = false,
        val workPackages: List<WorkPackage> = emptyList(),
    )

    init {
        mutableState.update { it.copy(isLoading = true) }
        coroutineScope.launch {
            workPackageRepository.observeAllWorkPackages().collect { workPackages ->
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        workPackages = workPackages,
                    )
                }
            }

        }
    }
}