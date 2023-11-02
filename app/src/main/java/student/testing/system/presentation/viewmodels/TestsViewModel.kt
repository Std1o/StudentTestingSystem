package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import godofappstates.presentation.viewmodel.StatesViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import stdio.godofappstates.core.delegates.StateFlowVar.Companion.stateFlowVar
import student.testing.system.common.Constants.COURSE_REVIEW_NAVIGATION
import student.testing.system.common.Utils
import student.testing.system.domain.MainRepository
import student.testing.system.domain.getResult.GetResultUseCase
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.states.operationStates.ResultState
import student.testing.system.domain.states.operationStates.protect
import student.testing.system.models.CourseResponse
import student.testing.system.models.Test
import student.testing.system.models.TestResult
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.contentState.TestsContentState
import javax.inject.Inject
import javax.inject.Named
import kotlin.properties.Delegates

@HiltViewModel
class TestsViewModel @Inject constructor(
    @Named(COURSE_REVIEW_NAVIGATION) private val courseNavigator: AppNavigator,
    private val repository: MainRepository,
    private val getResultUseCase: GetResultUseCase,
) : StatesViewModel() {

    private val _contentState = MutableStateFlow(TestsContentState())
    val contentState = _contentState.asStateFlow()
    private var contentStateVar by stateFlowVar(_contentState)

    private val _resultReviewChannel = Channel<TestResult>()
    val resultReviewFlow = _resultReviewChannel.receiveAsFlow()

    lateinit var course: CourseResponse

    var courseId: Int by Delegates.observable(-1) { _, oldValue, newValue ->
        if (oldValue != newValue) getTests()
    }

    val isUserModerator by lazy {
        Utils.isUserModerator(course)
    }

    private fun getTests() {
        viewModelScope.launch {
            loadData { repository.getTests(courseId) }.collect {
                contentStateVar = contentStateVar.copy(tests = it)
            }
        }
    }

    fun navigateToTestCreation(course: CourseResponse) {
        courseNavigator.tryNavigateTo(Destination.TestCreationHostScreen(course = course))
    }

    fun onTestAdded(test: Test) {
        contentStateVar = contentStateVar.copy(
            tests = LoadableData.Success(
                listOf(
                    *(contentStateVar.tests as LoadableData.Success).data.toTypedArray(),
                    test
                )
            )
        )
    }

    fun deleteTest(testId: Int, courseId: Int) {
        viewModelScope.launch {
            executeEmptyOperation({ repository.deleteTest(testId = testId, courseId = courseId) }) {
                val newTests = (contentStateVar.tests as LoadableData.Success)
                    .data.filter { it.id != testId }
                contentStateVar =
                    contentStateVar.copy(tests = LoadableData.Success(newTests))
            }.protect()
        }
    }

    fun onTestClicked(test: Test) {
        if (isUserModerator) {
            courseNavigator.tryNavigateTo(Destination.ResultsReviewScreen())
        } else {
            viewModelScope.launch {
                val requestResult = executeOperation({
                    getResultUseCase(testId = test.id, courseId = test.courseId)
                }, TestResult::class)
                if (requestResult is OperationState.Success) {
                    _resultReviewChannel.send(requestResult.data)
                    courseNavigator.tryNavigateTo(Destination.ResultReviewScreen())
                }
                if (requestResult is ResultState.NoResult) {
                    courseNavigator.tryNavigateTo(Destination.TestPassingScreen())
                }
            }
        }
    }

    // TODO remove
    fun getResult(testId: Int, courseId: Int): StateFlow<ResultState<TestResult>> {
        val stateFlow = MutableStateFlow<ResultState<TestResult>>(OperationState.Loading())
        viewModelScope.launch {
            stateFlow.emit(getResultUseCase(testId, courseId))
        }
        return stateFlow
    }
}