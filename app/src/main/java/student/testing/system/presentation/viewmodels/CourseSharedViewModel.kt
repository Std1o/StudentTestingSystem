package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.models.CourseResponse

class CourseSharedViewModel : ViewModel() {

    val courseFlow = MutableStateFlow(CourseResponse("", 0, "", "", listOf()))

    fun setCourse(course: CourseResponse) {
        viewModelScope.launch {
            courseFlow.tryEmit(course)
        }
    }
}