package student.testing.system.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import student.testing.system.api.network.DataState
import student.testing.system.api.network.MainRepository
import student.testing.system.common.Utils
import student.testing.system.models.CourseResponse
import student.testing.system.models.Question
import student.testing.system.models.Test
import student.testing.system.models.UserQuestion
import javax.inject.Inject

@HiltViewModel
class TestPassingViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val userQuestions: ArrayList<UserQuestion> = arrayListOf()

    fun calculateResult(testId: Int, courseId: Int): StateFlow<DataState<Int>> {
        val stateFlow = MutableStateFlow<DataState<Int>>(DataState.Loading)
        viewModelScope.launch {
            repository.calculateResult(testId, courseId, userQuestions).catch {
                stateFlow.emit(DataState.Error(it.message ?: " "))
            }.collect {
                if (it.isSuccessful) {
                    stateFlow.emit(DataState.Success(0))
                } else {
                    val errorMessage = Utils.encodeErrorCode(it.errorBody())
                    stateFlow.emit(DataState.Error(errorMessage))
                }
            }
        }
        return stateFlow
    }
}