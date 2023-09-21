package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import student.testing.system.domain.DataState
import student.testing.system.domain.MainRepository
import student.testing.system.domain.getResult.GetResultUseCase
import student.testing.system.domain.getResult.ResultState
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

    fun getTests(courseId: Int): StateFlow<DataState<List<Test>>> {
        val stateFlow = MutableStateFlow<DataState<List<Test>>>(DataState.Loading)
        viewModelScope.launch {
            stateFlow.emit(repository.getTests(courseId))
        }
        return stateFlow
    }

    fun createTest(
        courseId: Int,
        name: String,
        creationTIme: String,
        questions: List<Question>
    ): StateFlow<DataState<Test>> {
        val stateFlow = MutableStateFlow<DataState<Test>>(DataState.Loading)
        viewModelScope.launch {
            val requestResult = repository
                .createTest(TestCreationReq(courseId, name, creationTIme, questions))
            stateFlow.emit(requestResult)
        }
        return stateFlow
    }

    fun deleteTest(testId: Int, courseId: Int): StateFlow<DataState<Int>> {
        val stateFlow = MutableStateFlow<DataState<Int>>(DataState.Loading)
        viewModelScope.launch {
            val requestResult = repository.deleteTest(testId, courseId)
            if (requestResult is DataState.Empty) {
                stateFlow.emit(DataState.Success(testId))
            } else {
                stateFlow.emit(requestResult as DataState.Error)
            }
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