package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import student.testing.system.models.CourseResponse
import student.testing.system.domain.DataState
import student.testing.system.data.MainRepository
import student.testing.system.common.Utils
import javax.inject.Inject

@HiltViewModel
class CourseAddingViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    fun createCourse(name: String): StateFlow<DataState<CourseResponse>> {
        val stateFlow = MutableStateFlow<DataState<CourseResponse>>(DataState.Loading)
        viewModelScope.launch {
            repository.createCourse(name).collect {
                stateFlow.emit(it)
            }
        }
        return stateFlow
    }

    fun joinCourse(courseCode: String): StateFlow<DataState<CourseResponse>> {
        val stateFlow = MutableStateFlow<DataState<CourseResponse>>(DataState.Loading)
        viewModelScope.launch {
            repository.joinCourse(courseCode).collect {
                stateFlow.emit(it)
            }
        }
        return stateFlow
    }
}