package student.testing.system.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import student.testing.system.models.TestResult
import student.testing.system.api.network.DataState
import student.testing.system.api.network.MainRepository
import student.testing.system.common.Utils
import student.testing.system.models.ParticipantsResults
import student.testing.system.models.Question
import student.testing.system.models.Test
import javax.inject.Inject

@HiltViewModel
class TestsViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    fun getTests(courseId: Int): StateFlow<DataState<List<Test>>> {
        val stateFlow = MutableStateFlow<DataState<List<Test>>>(DataState.Loading)
        viewModelScope.launch {
            repository.getTests(courseId).catch {
                stateFlow.emit(DataState.Error(it.message ?: " "))
            }.collect {
                if (it.isSuccessful) {
                    stateFlow.emit(DataState.Success(it.body()!!))
                } else {
                    val errorMessage = Utils.encodeErrorCode(it.errorBody())
                    stateFlow.emit(DataState.Error(errorMessage))
                }
            }
        }
        return stateFlow
    }

    fun createTest(courseId: Int, name: String, creationTIme: String, questions: List<Question>): StateFlow<DataState<Test>> {
        val stateFlow = MutableStateFlow<DataState<Test>>(DataState.Loading)
        viewModelScope.launch {
            repository.createTest(courseId, name, creationTIme, questions).catch {
                stateFlow.emit(DataState.Error(it.message ?: " "))
            }.collect {
                if (it.isSuccessful) {
                    stateFlow.emit(DataState.Success(it.body()!!))
                } else {
                    val errorMessage = Utils.encodeErrorCode(it.errorBody())
                    stateFlow.emit(DataState.Error(errorMessage))
                }
            }
        }
        return stateFlow
    }

    fun deleteTest(testId: Int, courseId: Int, courseOwnerId: Int): StateFlow<DataState<Int>> {
        val stateFlow = MutableStateFlow<DataState<Int>>(DataState.Loading)
        viewModelScope.launch {
            repository.deleteTest(testId, courseId, courseOwnerId).catch {
                stateFlow.emit(DataState.Error(it.message ?: " "))
            }.collect {
                if (it.isSuccessful) {
                    stateFlow.emit(DataState.Success(testId))
                } else {
                    Log.d("errorCode", it.code().toString())
                    val errorMessage = Utils.encodeErrorCode(it.errorBody())
                    stateFlow.emit(DataState.Error(errorMessage))
                }
            }
        }
        return stateFlow
    }

    fun getResult(testId: Int, courseId: Int): StateFlow<DataState<TestResult>> {
        val stateFlow = MutableStateFlow<DataState<TestResult>>(DataState.Loading)
        viewModelScope.launch {
            repository.getResult(testId, courseId).catch {
                stateFlow.emit(DataState.Error(it.message ?: " "))
            }.collect {
                if (it.isSuccessful) {
                    stateFlow.emit(DataState.Success(it.body()!!))
                } else {
                    Log.d("errorCode", it.code().toString())
                    val errorMessage = Utils.encodeErrorCode(it.errorBody())
                    stateFlow.emit(DataState.Error(errorMessage, it.code()))
                }
            }
        }
        return stateFlow
    }

    fun getResults(testId: Int, courseId: Int, courseOwnerId: Int): StateFlow<DataState<ParticipantsResults>> {
        val stateFlow = MutableStateFlow<DataState<ParticipantsResults>>(DataState.Loading)
        viewModelScope.launch {
            repository.getResults(testId, courseId, courseOwnerId).catch {
                stateFlow.emit(DataState.Error(it.message ?: " "))
            }.collect {
                if (it.isSuccessful) {
                    stateFlow.emit(DataState.Success(it.body()!!))
                } else {
                    Log.d("errorCode", it.code().toString())
                    val errorMessage = Utils.encodeErrorCode(it.errorBody())
                    stateFlow.emit(DataState.Error(errorMessage))
                }
            }
        }
        return stateFlow
    }
}