package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import lilith.domain.SingleEventFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.domain.models.CourseResponse
import student.testing.system.domain.models.Test

class CourseSharedViewModel : ViewModel() {

    private val _courseFlow = MutableStateFlow(CourseResponse("", 0, "", "", listOf()))
    val courseFlow = _courseFlow.asStateFlow()
    private val _testEvent = SingleEventFlow<Test>()
    val testFlow = _testEvent.asSharedFlow()

    fun setCourse(course: CourseResponse) {
        viewModelScope.launch {
            _courseFlow.value = course
        }
    }

    fun onTestAdded(test: Test) {
        _testEvent.tryEmit(test)
    }
}