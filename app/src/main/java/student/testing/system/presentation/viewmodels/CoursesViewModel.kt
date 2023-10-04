package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import student.testing.system.annotations.NotScreenState
import student.testing.system.common.launchRequest
import student.testing.system.common.makeOperation
import student.testing.system.domain.MainRepository
import student.testing.system.domain.states.OperationState
import student.testing.system.domain.states.RequestState
import student.testing.system.domain.usecases.CreateCourseUseCase
import student.testing.system.domain.usecases.JoinCourseUseCase
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
    private val appNavigator: AppNavigator,
    private val createCourseUseCase: CreateCourseUseCase,
    private val joinCourseUseCase: JoinCourseUseCase
) : OperationViewModel<OperationState<CourseResponse>, CourseResponse>() {

    private val _contentState = MutableStateFlow(CoursesContentState())
    val contentState: StateFlow<CoursesContentState> = _contentState.asStateFlow()
    private var contentStateVar
        get() = _contentState.value
        set(value) {
            _contentState.value = value
        }

    init {
        getCourses()
    }

    private fun getCourses() {
        viewModelScope.launch {
            contentStateVar = contentStateVar.copy(
                courses = launchRequest(
                    call = { repository.getCourses() },
                    onLoading = { contentStateVar = contentStateVar.copy(courses = it) }
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
        _contentState.update { it.copy(isLoggedOut = true) }
    }

    fun onCourseClicked(course: CourseResponse) {
        appNavigator.tryNavigateTo(Destination.CourseReviewScreen(course = course))
    }

    fun createCourse(name: String) {
        viewModelScope.launch {
            executeOperation({ createCourseUseCase(name) }) { courseResponse ->
                addCourseToContent(courseResponse)
            }
        }
    }

    fun joinCourse(courseCode: String) {
        viewModelScope.launch {
            executeOperation({ joinCourseUseCase(courseCode) }) { courseResponse ->
                addCourseToContent(courseResponse)
            }
        }
    }

    @OptIn(NotScreenState::class)
    private fun addCourseToContent(course: CourseResponse) {
        contentStateVar = contentStateVar.copy(
            courses = RequestState.Success(
                listOf(
                    *(contentStateVar.courses as RequestState.Success).data.toTypedArray(),
                    course
                )
            )
        )
    }
}