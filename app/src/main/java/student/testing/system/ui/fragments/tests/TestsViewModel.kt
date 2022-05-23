package student.testing.system.ui.fragments.tests

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import student.testing.system.api.models.courses.CourseResponse
import student.testing.system.api.models.tests.TestResult
import student.testing.system.api.network.DataState
import student.testing.system.api.network.MainRepository
import student.testing.system.common.Utils
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
                    stateFlow.emit(DataState.Error(errorMessage))
                }
            }
        }
        return stateFlow
    }
}