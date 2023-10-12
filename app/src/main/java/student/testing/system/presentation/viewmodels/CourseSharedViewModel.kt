package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.models.CourseResponse
import student.testing.system.models.Test

class CourseSharedViewModel : ViewModel() {

    val courseFlow = MutableStateFlow(CourseResponse("", 0, "", "", listOf()))
    private val _testChannel = Channel<Test>()
    val testFlow = _testChannel.receiveAsFlow()

    fun setCourse(course: CourseResponse) {
        viewModelScope.launch {
            courseFlow.tryEmit(course)
        }
    }

    fun onTestAdded(test: Test) {
        _testChannel.trySend(test)
    }
}