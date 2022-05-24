package student.testing.system.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.api.models.courses.CourseResponse
import student.testing.system.api.network.DataState
import student.testing.system.common.Utils

class CourseSharedViewModel : ViewModel() {

    val courseFlow = MutableSharedFlow<CourseResponse>(replay = 1, onBufferOverflow= BufferOverflow.DROP_OLDEST)

    fun setCourse(course: CourseResponse) {
        viewModelScope.launch {
            courseFlow.tryEmit(course)
        }
    }
}