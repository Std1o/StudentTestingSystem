package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import student.testing.system.common.makeOperation
import student.testing.system.domain.MainRepository
import student.testing.system.domain.states.RequestState
import student.testing.system.models.TestResult
import student.testing.system.models.UserQuestion
import javax.inject.Inject

// TODO сделать поле StateFlow и убрать StateFlow с методов, либо написать, почему этого сделать нельзя
@HiltViewModel
class TestPassingViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    val userQuestions: ArrayList<UserQuestion> = arrayListOf()

    fun calculateResult(testId: Int, courseId: Int): StateFlow<RequestState<Int>> {
        val stateFlow = MutableStateFlow<RequestState<Int>>(RequestState.Loading)
        viewModelScope.launch {
            val requestResult =
                makeOperation(repository.calculateResult(testId, courseId, userQuestions), 0)
            stateFlow.emit(requestResult)
        }
        return stateFlow
    }

    fun calculateDemoResult(courseId: Int, testId: Int): StateFlow<RequestState<TestResult>> {
        val stateFlow = MutableStateFlow<RequestState<TestResult>>(RequestState.Loading)
        viewModelScope.launch {
            val requestResult = repository.calculateDemoResult(courseId, testId, userQuestions)
            stateFlow.emit(requestResult)
        }
        return stateFlow
    }

    fun getResult(testId: Int, courseId: Int): StateFlow<RequestState<TestResult>> {
        val stateFlow = MutableStateFlow<RequestState<TestResult>>(RequestState.Loading)
        viewModelScope.launch {
            val requestResult = repository.getResult(testId, courseId)
            stateFlow.emit(requestResult)
        }
        return stateFlow
    }
}