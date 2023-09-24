package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import student.testing.system.models.CourseResponse
import student.testing.system.domain.states.DataState
import student.testing.system.domain.MainRepository
import javax.inject.Inject

// TODO сделать поле StateFlow и убрать StateFlow с методов, либо написать, почему этого сделать нельзя
@HiltViewModel
class CoursesViewModel @Inject constructor(private val repository: MainRepository) :
    BaseViewModel<List<CourseResponse>>() {

    init {
        getCourses()
    }

    private fun getCourses() {
        viewModelScope.launch {
            launchRequest(repository.getCourses())
        }
    }

    fun deleteCourse(courseId: Int): StateFlow<DataState<Int>> {
        val stateFlow = MutableStateFlow<DataState<Int>>(DataState.Loading)
        viewModelScope.launch {
            val requestResult = repository.deleteCourse(courseId)
            if (requestResult is DataState.Empty) {
                stateFlow.emit(DataState.Success(courseId))
            } else {
                stateFlow.emit(requestResult as DataState.Error)
            }
        }
        return stateFlow
    }
}