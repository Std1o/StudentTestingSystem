package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import godofappstates.presentation.viewmodel.StatesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import stdio.godofappstates.core.delegates.StateFlowVar.Companion.stateFlowVar
import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.models.Test
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.presentation.ui.models.FiltersContainer
import student.testing.system.presentation.ui.models.contentState.ResultsContentState
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(private val repository: TestsRepository) :
    StatesViewModel() {

    private val _contentState = MutableStateFlow(ResultsContentState())
    val contentState = _contentState.asStateFlow()
    private var contentStateVar by stateFlowVar(_contentState)

    private lateinit var test: Test
    var searchPrefix: String? = null
    val filtersContainer = FiltersContainer()

    fun setInitialData(test: Test) {
        this.test = test
        getResults()
    }

    fun getResults() {
        val params = with(filtersContainer) {
            TestResultsRequestParams(
                onlyMaxResult = showOnlyMaxResults, searchPrefix = searchPrefix,
                upperBound = if (ratingRangeEnabled) upperBound else null,
                lowerBound = if (ratingRangeEnabled) lowerBound else null,
                scoreEquals = if (scoreEqualsEnabled) scoreEqualsValue else null,
                dateFrom = dateFrom, dateTo = dateTo, ordering = orderingType
            )
        }

        viewModelScope.launch {
            loadData { repository.getResults(test.id, test.courseId, params) }.collect {
                contentStateVar = contentStateVar.copy(results = it)
                if (it is LoadableData.Success && filtersContainer.maxScore == 0) {
                    filtersContainer.maxScore = it.data.maxScore
                    if (filtersContainer.maxScore == 0) {
                        filtersContainer.maxScore = 100 // this can happen if there are no results
                    }
                }
            }
        }
    }
}