package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.domain.models.CourseResponse
import student.testing.system.domain.models.Test

class CourseSharedViewModel : ViewModel() {

    private val _courseFlow = MutableStateFlow(CourseResponse("", 0, "", "", listOf()))
    val courseFlow = _courseFlow.asStateFlow()
    private val _testChannel = Channel<Test>()
    val testFlow = _testChannel.receiveAsFlow()

    fun setCourse(course: CourseResponse) {
        viewModelScope.launch {
            _courseFlow.value = course
        }
    }

    fun onTestAdded(test: Test) {
        _testChannel.trySend(test)
    }
}