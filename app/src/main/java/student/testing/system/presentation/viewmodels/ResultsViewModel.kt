package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stdio.lilith.core.delegates.StateFlowVar.Companion.stateFlowVar
import student.testing.system.domain.models.ParticipantsResults
import student.testing.system.domain.models.Test
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.webSockets.WebsocketEvent
import student.testing.system.presentation.ui.models.FilterIntent
import student.testing.system.presentation.ui.models.FiltersContainer
import student.testing.system.presentation.ui.models.contentState.ResultsContentState
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    val repository: TestsRepository
) : ViewModel() {

    private val _contentState = MutableStateFlow(ResultsContentState())
    val contentState = _contentState.asStateFlow()
    private var contentStateVar by stateFlowVar(_contentState)

    private lateinit var test: Test
    var searchPrefix: String? = null
    private val _filtersContainer = MutableStateFlow(FiltersContainer())
    val filtersContainer = _filtersContainer.asStateFlow()

    fun setInitialData(test: Test) {
        this.test = test
        getResults()
    }

    fun getResults() {
        contentStateVar = ResultsContentState(LoadableData.Loading())

        viewModelScope.launch {
            repository.getResults(test.id, test.courseId, getTestResultsRequestParams()).collect {
                if (it is WebsocketEvent.Receive<ParticipantsResults>) {
                    contentStateVar = contentStateVar.copy(
                        results = LoadableData.Success(it.data)
                    )
                    configureMaxScore(it.data)
                }
                if (it is WebsocketEvent.Disconnected) {
                    contentStateVar = contentStateVar.copy(
                        results = LoadableData.Error(it.reason)
                    )
                }
            }
        }
    }

    private fun getTestResultsRequestParams() = with(filtersContainer.value) {
        TestResultsRequestParams(
            onlyMaxResult = showOnlyMaxResults, searchPrefix = searchPrefix,
            upperBound = if (ratingRangeEnabled) upperBound else null,
            lowerBound = if (ratingRangeEnabled) lowerBound else null,
            scoreEquals = if (scoreEqualsEnabled) scoreEqualsValue else null,
            dateFrom = dateFrom, dateTo = dateTo, ordering = orderingType
        )
    }

    private fun configureMaxScore(participantsResults: ParticipantsResults) =
        with(filtersContainer.value) {
            if (maxScore == 0) {
                _filtersContainer.update { it.copyWithMaxScore(maxScore = participantsResults.maxScore) }
                if (participantsResults.maxScore == 0) {
                    _filtersContainer.update { it.copyWithMaxScore(maxScore = 100) } // this can happen if there are no results
                }
            }
        }

    fun updateFilter(intent: FilterIntent) {
        when (intent) {
            is FilterIntent.UpdateShowOnlyMaxResults -> {
                _filtersContainer.update { it.copy(showOnlyMaxResults = intent.showOnlyMaxResults) }
            }

            is FilterIntent.UpdateRatingRangeEnabled -> {
                _filtersContainer.update { it.copy(ratingRangeEnabled = intent.ratingRangeEnabled) }
            }
        }
    }
}