package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import student.testing.system.common.launchRequest
import student.testing.system.common.makeOperation
import student.testing.system.domain.MainRepository
import student.testing.system.domain.states.RequestState
import student.testing.system.models.CourseResponse
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.CoursesContentState
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject

// TODO сделать поле StateFlow и убрать StateFlow с методов, либо написать, почему этого сделать нельзя
@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val repository: MainRepository,
    private val prefUtils: PrefsUtils,
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoursesContentState())
    val uiState: StateFlow<CoursesContentState> = _uiState.asStateFlow()

    init {
        getCourses()
    }

    private fun getCourses() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                courses = launchRequest(
                    call = { repository.getCourses() },
                    onLoading = { _uiState.value = _uiState.value.copy(courses = it) }
                )
            )
        }
    }

    fun deleteCourse(courseId: Int): StateFlow<RequestState<Int>> {
        val stateFlow = MutableStateFlow<RequestState<Int>>(RequestState.Loading)
        viewModelScope.launch {
            val state = makeOperation(repository.deleteCourse(courseId), courseId)
            stateFlow.emit(state)
        }
        return stateFlow
    }

    fun logout() {
        prefUtils.clearData()
        _uiState.update { it.copy(isLoggedOut = true) }
    }

    fun onCourseClicked(course: CourseResponse) {
        appNavigator.tryNavigateTo(Destination.CourseReviewScreen(course = course))
    }
}