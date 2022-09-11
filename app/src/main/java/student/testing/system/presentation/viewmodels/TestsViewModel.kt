package student.testing.system.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import student.testing.system.models.TestResult
import student.testing.system.domain.DataState
import student.testing.system.data.MainRepository
import student.testing.system.common.Utils
import student.testing.system.models.Question
import student.testing.system.models.Test
import javax.inject.Inject

@HiltViewModel
class TestsViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    fun getTests(courseId: Int): StateFlow<DataState<List<Test>>> {
        val stateFlow = MutableStateFlow<DataState<List<Test>>>(DataState.Loading)
        viewModelScope.launch {
            repository.getTests(courseId).collect {
                stateFlow.emit(it)
            }
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
            repository.createTest(courseId, name, creationTIme, questions).collect {
                stateFlow.emit(it)
            }
        }
        return stateFlow
    }

    fun deleteTest(testId: Int, courseId: Int, courseOwnerId: Int): StateFlow<DataState<Int>> {
        val stateFlow = MutableStateFlow<DataState<Int>>(DataState.Loading)
        viewModelScope.launch {
            repository.deleteTest(testId, courseId, courseOwnerId).collect {
                if (it is DataState.Success) {
                    stateFlow.emit(DataState.Success(testId))
                } else {
                    stateFlow.emit(it as DataState.Error)
                }
            }
        }
        return stateFlow
    }

    fun getResult(testId: Int, courseId: Int): StateFlow<DataState<TestResult>> {
        val stateFlow = MutableStateFlow<DataState<TestResult>>(DataState.Loading)
        viewModelScope.launch {
            repository.getResult(testId, courseId).collect {
                stateFlow.emit(it)
            }
        }
        return stateFlow
    }
}