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
import student.testing.system.domain.MainRepository
import student.testing.system.domain.operationTypes.CourseAddingOperations
import student.testing.system.domain.states.RequestState
import student.testing.system.domain.states.ValidatableOperationState
import student.testing.system.domain.usecases.CreateCourseUseCase
import student.testing.system.domain.usecases.JoinCourseUseCase
import student.testing.system.models.CourseResponse
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.CoursesContentState
import student.testing.system.presentation.ui.stateWrapper.UIStateWrapper
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
) : OperationViewModel<CourseResponse>() {

    private val _lastValidationStateWrapper =
        MutableStateFlow<UIStateWrapper<ValidatableOperationState<CourseResponse>, CourseResponse>>(
            UIStateWrapper(RequestState.NoState)
        )
    val lastValidationStateWrapper: StateFlow<UIStateWrapper<ValidatableOperationState<CourseResponse>, CourseResponse>> =
        _lastValidationStateWrapper.asStateFlow()

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

    @OptIn(NotScreenState::class)
    fun deleteCourse(courseId: Int) {
        viewModelScope.launch {
            executeOperation(
                call = { repository.deleteCourse(courseId) },
                onEmpty = {
                    val newCourses = (contentStateVar.courses as RequestState.Success)
                        .data.filter { it.id != courseId }
                    contentStateVar =
                        contentStateVar.copy(courses = RequestState.Success(newCourses))
                }
            ).protect()
        }
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
            val requestResult = executeOperation(
                call = { createCourseUseCase(name) },
                operationType = CourseAddingOperations.CREATE_COURSE
            ) { courseResponse ->
                addCourseToContent(courseResponse)
            }
            _lastValidationStateWrapper.value = UIStateWrapper(requestResult)
        }
    }

    fun joinCourse(courseCode: String) {
        viewModelScope.launch {
            val requestResult =
                executeOperation(
                    call = { joinCourseUseCase(courseCode) },
                    operationType = CourseAddingOperations.JOIN_COURSE
                ) { courseResponse ->
                    addCourseToContent(courseResponse)
                }
            _lastValidationStateWrapper.value = UIStateWrapper(requestResult)
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