package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import stdio.lilith.core.delegates.StateFlowVar.Companion.stateFlowVar
import student.testing.system.domain.models.ParticipantsResults
import student.testing.system.domain.models.Test
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.webSockets.WebsocketEvent
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
    val filtersContainer = FiltersContainer()

    fun setInitialData(test: Test) {
        this.test = test
        getResults()
    }

    fun getResults() {
        contentStateVar = ResultsContentState(LoadableData.Loading())

        viewModelScope.launch {
            repository.getResults(test.id, test.courseId, getTestResultsRequestParams()).collect {
                if (it is WebsocketEvent.Receive) {
                    val dataJson = Gson().fromJson(
                        it.data,
                        ParticipantsResults::class.java
                    )
                    contentStateVar = contentStateVar.copy(
                        results = LoadableData.Success(dataJson)
                    )
                    configureMaxScore(dataJson)
                }
            }
        }
    }

    private fun getTestResultsRequestParams() = with(filtersContainer) {
        TestResultsRequestParams(
            onlyMaxResult = showOnlyMaxResults, searchPrefix = searchPrefix,
            upperBound = if (ratingRangeEnabled) upperBound else null,
            lowerBound = if (ratingRangeEnabled) lowerBound else null,
            scoreEquals = if (scoreEqualsEnabled) scoreEqualsValue else null,
            dateFrom = dateFrom, dateTo = dateTo, ordering = orderingType
        )
    }

    private fun configureMaxScore(participantsResults: ParticipantsResults) {
        if (filtersContainer.maxScore == 0) {
            filtersContainer.maxScore = participantsResults.maxScore
            if (filtersContainer.maxScore == 0) {
                filtersContainer.maxScore = 100 // this can happen if there are no results
            }
        }
    }
}