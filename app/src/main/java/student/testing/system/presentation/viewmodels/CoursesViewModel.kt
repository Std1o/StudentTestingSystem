package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import student.testing.system.common.Constants.LAUNCH_NAVIGATION
import student.testing.system.delegates.StateFlowVar.Companion.stateFlowVar
import student.testing.system.domain.MainRepository
import student.testing.system.domain.operationTypes.CourseAddingOperations
import student.testing.system.domain.states.LoadableData
import student.testing.system.domain.states.ValidatableOperationState
import student.testing.system.domain.usecases.CreateCourseUseCase
import student.testing.system.domain.usecases.JoinCourseUseCase
import student.testing.system.models.CourseResponse
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.contentState.CoursesContentState
import student.testing.system.presentation.ui.stateWrapper.StateWrapper
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val repository: MainRepository,
    private val prefUtils: PrefsUtils,
    @Named(LAUNCH_NAVIGATION) private val appNavigator: AppNavigator,
    private val createCourseUseCase: CreateCourseUseCase,
    private val joinCourseUseCase: JoinCourseUseCase
) : StatesViewModel() {

    private val _lastValidationStateWrapper =
        StateWrapper.mutableStateFlow<ValidatableOperationState<CourseResponse>>()
    val lastValidationStateWrapper = _lastValidationStateWrapper.asStateFlow()

    // TODO проверить, нужно ли здесь флоу, мб достаточно mutableStateOf()
    private val _contentState = MutableStateFlow(CoursesContentState())
    val contentState = _contentState.asStateFlow()

    private val defaultType = CourseResponse::class
    private var contentStateVar by stateFlowVar(_contentState)

    init {
        getCourses()
    }

    fun getCourses() {
        viewModelScope.launch {
            loadData { repository.getCourses() }.collect {
                contentStateVar = contentStateVar.copy(courses = it)
            }
        }
    }

    fun deleteCourse(courseId: Int) {
        viewModelScope.launch {
            executeEmptyOperation({ repository.deleteCourse(courseId) }) {
                val newCourses = (contentStateVar.courses as LoadableData.Success)
                    .data.filter { it.id != courseId }
                contentStateVar =
                    contentStateVar.copy(courses = LoadableData.Success(newCourses))
            }.protect()
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
            executeOperation(
                call = { createCourseUseCase(name) },
                operationType = CourseAddingOperations.CREATE_COURSE,
                type = defaultType
            ) { courseResponse ->
                addCourseToContent(courseResponse)
            }.collect {
                _lastValidationStateWrapper.value = StateWrapper(it)
            }
        }
    }

    fun joinCourse(courseCode: String) {
        viewModelScope.launch {
            executeOperation(
                call = { joinCourseUseCase(courseCode) },
                operationType = CourseAddingOperations.JOIN_COURSE,
                type = defaultType
            ) { courseResponse ->
                addCourseToContent(courseResponse)
            }.collect {
                _lastValidationStateWrapper.value = StateWrapper(it)
            }
        }
    }

    private fun addCourseToContent(course: CourseResponse) {
        try {
            contentStateVar = contentStateVar.copy(
                courses = LoadableData.Success(
                    listOf(
                        *(contentStateVar.courses as LoadableData.Success).data.toTypedArray(),
                        course
                    )
                )
            )
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }
}