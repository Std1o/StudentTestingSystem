package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import student.testing.system.models.CourseResponse
import student.testing.system.domain.DataState
import student.testing.system.domain.MainRepository
import student.testing.system.domain.usecases.CreateCourseUseCase
import student.testing.system.domain.usecases.JoinCourseUseCase
import javax.inject.Inject

@HiltViewModel
class CourseAddingViewModel @Inject constructor(
    private val createCourseUseCase: CreateCourseUseCase,
    private val joinCourseUseCase: JoinCourseUseCase
) :
    ViewModel() {

    fun createCourse(name: String): StateFlow<DataState<CourseResponse>> {
        val stateFlow = MutableStateFlow<DataState<CourseResponse>>(DataState.Loading)
        viewModelScope.launch {
            createCourseUseCase(name).collect {
                stateFlow.emit(it)
            }
        }
        return stateFlow
    }

    fun joinCourse(courseCode: String): StateFlow<DataState<CourseResponse>> {
        val stateFlow = MutableStateFlow<DataState<CourseResponse>>(DataState.Loading)
        viewModelScope.launch {
            joinCourseUseCase(courseCode).collect {
                stateFlow.emit(it)
            }
        }
        return stateFlow
    }
}