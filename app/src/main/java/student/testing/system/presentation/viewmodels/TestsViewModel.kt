package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import student.testing.system.common.makeOperation
import student.testing.system.domain.MainRepository
import student.testing.system.domain.getResult.GetResultUseCase
import student.testing.system.domain.getResult.ResultState
import student.testing.system.domain.states.OperationState
import student.testing.system.models.CourseResponse
import student.testing.system.models.Question
import student.testing.system.models.Test
import student.testing.system.models.TestCreationReq
import student.testing.system.models.TestResult
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.TestsContentState
import javax.inject.Inject
import kotlin.properties.Delegates

// TODO сделать поле StateFlow и убрать StateFlow с методов, либо написать, почему этого сделать нельзя
@HiltViewModel
class TestsViewModel @Inject constructor(
    val appNavigator: AppNavigator,
    private val repository: MainRepository,
    private val getResultUseCase: GetResultUseCase,
) : StatesViewModel() {

    private val _contentState = MutableStateFlow(TestsContentState())
    val contentState = _contentState.asStateFlow()
    private var contentStateVar
        get() = _contentState.value
        set(value) {
            _contentState.value = value
        }

    var courseId: Int by Delegates.observable(-1) { _, oldValue, newValue ->
        if (oldValue != newValue) getTests()
    }

    private fun getTests() {
        viewModelScope.launch {
            loadData { repository.getTests(courseId) }.collect {
                contentStateVar = contentStateVar.copy(tests = it)
            }
        }
    }

    fun onAddBtnClicked(course: CourseResponse) {
        appNavigator.tryNavigateTo(Destination.TestCreationScreen(course = course))
    }

    // TODO мб переместить в TestCreationViewModel
    fun createTest(
        courseId: Int,
        name: String,
        creationTIme: String,
        questions: List<Question>
    ): StateFlow<OperationState<Test>> {
        val stateFlow = MutableStateFlow<OperationState<Test>>(OperationState.Loading())
        viewModelScope.launch {
            val requestResult = repository
                .createTest(TestCreationReq(courseId, name, creationTIme, questions))
            stateFlow.emit(requestResult)
        }
        return stateFlow
    }

    fun deleteTest(testId: Int, courseId: Int): StateFlow<OperationState<Int>> {
        val stateFlow = MutableStateFlow<OperationState<Int>>(OperationState.Loading())
        viewModelScope.launch {
            val requestResult = makeOperation(repository.deleteTest(testId, courseId), testId)
            stateFlow.emit(requestResult)
        }
        return stateFlow
    }

    fun getResult(testId: Int, courseId: Int): StateFlow<ResultState<TestResult>> {
        val stateFlow = MutableStateFlow<ResultState<TestResult>>(ResultState.Loading)
        viewModelScope.launch {
            stateFlow.emit(getResultUseCase(testId, courseId))
        }
        return stateFlow
    }
}