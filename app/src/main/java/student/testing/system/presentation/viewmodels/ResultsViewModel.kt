package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lilith.domain.SingleEventFlow
import lilith.presentation.viewmodel.StatesViewModel
import stdio.lilith.core.delegates.StateFlowVar.Companion.stateFlowVar
import student.testing.system.common.CourseReviewNavigation
import student.testing.system.common.Utils
import student.testing.system.domain.models.Course
import student.testing.system.domain.models.ParticipantResult
import student.testing.system.domain.models.ParticipantsResults
import student.testing.system.domain.models.Test
import student.testing.system.domain.models.TestResult
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.usecases.GetResultUseCase
import student.testing.system.domain.webSockets.WebsocketEvent
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.FilterIntent
import student.testing.system.presentation.ui.models.FiltersContainer
import student.testing.system.presentation.ui.models.contentState.ResultsContentState
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    @CourseReviewNavigation private val courseNavigator: AppNavigator,
    val repository: TestsRepository,
    private val getResultUseCase: GetResultUseCase,
) : StatesViewModel() {

    private val _contentState = MutableStateFlow(ResultsContentState())
    val contentState = _contentState.asStateFlow()
    private var contentStateVar by stateFlowVar(_contentState)

    private val _resultReviewEvent = SingleEventFlow<TestResult>()
    val resultReviewFlow = _resultReviewEvent.asSharedFlow()

    private lateinit var test: Test
    private lateinit var course: Course
    var searchPrefix: String? = null
    private val _filtersContainer = MutableStateFlow(FiltersContainer())
    val filtersContainer = _filtersContainer.asStateFlow()

    fun setInitialData(test: Test, course: Course) {
        this.test = test
        this.course = course
        getResults()
    }

    val isUserAModerator by lazy {
        Utils.isUserAModerator(course)
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

    fun getResult(participantResult: ParticipantResult) {
        if (!isUserAModerator) return
        viewModelScope.launch {
            val requestResult = executeOperation({
                repository.getParticipantResult(
                    testId = test.id,
                    courseId = test.courseId,
                    participantResult.userId
                )
            }, TestResult::class)
            if (requestResult is OperationState.Success) {
                _resultReviewEvent.emit(requestResult.data)
                courseNavigator.navigateTo(Destination.ResultReviewScreen())
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

    override fun onCleared() {
        super.onCleared()
        repository.closeResultsConnection()
    }
}