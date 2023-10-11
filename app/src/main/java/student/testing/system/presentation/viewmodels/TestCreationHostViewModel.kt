package student.testing.system.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import student.testing.system.domain.addQuestion.AddQuestionUseCase
import student.testing.system.domain.addQuestion.QuestionState
import student.testing.system.models.CourseResponse
import student.testing.system.models.Question
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TestCreationHostViewModel @Inject constructor(
    @Named("TestCreationNavigation") appNavigator: AppNavigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val navigationChannel = appNavigator.navigationChannel

    val course: CourseResponse =
        checkNotNull(savedStateHandle[Destination.CourseReviewScreen.COURSE_KEY])
}