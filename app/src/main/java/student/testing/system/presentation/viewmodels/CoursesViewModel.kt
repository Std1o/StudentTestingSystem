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
import student.testing.system.domain.MainRepository
import student.testing.system.common.Utils
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    fun getCourses(): StateFlow<DataState<List<CourseResponse>>> {
        val stateFlow = MutableStateFlow<DataState<List<CourseResponse>>>(DataState.Loading)
        viewModelScope.launch {
            repository.getCourses().collect {
                stateFlow.emit(it)
            }
        }
        return stateFlow
    }

    fun deleteCourse(courseId: Int, courseOwnerId: Int): StateFlow<DataState<Int>> {
        val stateFlow = MutableStateFlow<DataState<Int>>(DataState.Loading)
        viewModelScope.launch {
            repository.deleteCourse(courseId, courseOwnerId).collect {
                if (it is DataState.Empty) {
                    stateFlow.emit(DataState.Success(courseId))
                } else {
                    stateFlow.emit(it as DataState.Error)
                }
            }
        }
        return stateFlow
    }
}