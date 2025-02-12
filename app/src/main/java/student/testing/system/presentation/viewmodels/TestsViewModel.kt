package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import lilith.domain.SingleEventFlow
import lilith.presentation.viewmodel.StatesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import stdio.lilith.core.delegates.StateFlowVar.Companion.stateFlowVar
import student.testing.system.common.CourseReviewNavigation
import student.testing.system.common.Utils
import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.states.operationStates.ResultState
import student.testing.system.domain.states.operationStates.protect
import student.testing.system.domain.usecases.GetResultUseCase
import student.testing.system.domain.models.CourseResponse
import student.testing.system.domain.models.Test
import student.testing.system.domain.models.TestResult
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.contentState.TestsContentState
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class TestsViewModel @Inject constructor(
    @CourseReviewNavigation private val courseNavigator: AppNavigator,
    private val repository: TestsRepository,
    private val getResultUseCase: GetResultUseCase,
) : StatesViewModel() {

    private val _contentState = MutableStateFlow(TestsContentState())
    val contentState = _contentState.asStateFlow()
    private var contentStateVar by stateFlowVar(_contentState)

    private val _resultReviewEvent = SingleEventFlow<TestResult>()
    val resultReviewFlow = _resultReviewEvent.asSharedFlow()

    lateinit var course: CourseResponse

    var courseId: Int by Delegates.observable(-1) { _, oldValue, newValue ->
        if (oldValue != newValue) getTests()
    }

    val isUserAModerator by lazy {
        Utils.isUserAModerator(course)
    }

    fun getTests() {
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
        if (isUserAModerator) {
            courseNavigator.tryNavigateTo(Destination.ResultsReviewScreen())
        } else {
            viewModelScope.launch {
                val requestResult = executeOperation({
                    getResultUseCase(testId = test.id, courseId = test.courseId)
                }, TestResult::class)
                if (requestResult is OperationState.Success) {
                    _resultReviewEvent.emit(requestResult.data)
                    courseNavigator.tryNavigateTo(Destination.ResultReviewScreen())
                }
                if (requestResult is ResultState.NoResult) {
                    courseNavigator.tryNavigateTo(Destination.TestPassingScreen())
                }
            }
        }
    }

    fun onCheckOptionSelected() {
        courseNavigator.tryNavigateTo(Destination.TestPassingScreen())
    }
}