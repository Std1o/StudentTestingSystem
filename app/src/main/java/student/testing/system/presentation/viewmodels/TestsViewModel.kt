package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import student.testing.system.common.makeOperation
import student.testing.system.domain.MainRepository
import student.testing.system.domain.getResult.GetResultUseCase
import student.testing.system.domain.getResult.ResultState
import student.testing.system.domain.states.LoadableData
import student.testing.system.domain.states.OperationState
import student.testing.system.models.Question
import student.testing.system.models.Test
import student.testing.system.models.TestCreationReq
import student.testing.system.models.TestResult
import javax.inject.Inject

// TODO сделать поле StateFlow и убрать StateFlow с методов, либо написать, почему этого сделать нельзя
@HiltViewModel
class TestsViewModel @Inject constructor(
    private val repository: MainRepository,
    private val getResultUseCase: GetResultUseCase
) : ViewModel() {

    fun getTests(courseId: Int): StateFlow<LoadableData<List<Test>>> {
        val stateFlow = MutableStateFlow<LoadableData<List<Test>>>(LoadableData.Loading())
        viewModelScope.launch {
            stateFlow.emit(repository.getTests(courseId))
        }
        return stateFlow
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