package student.testing.system.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import student.testing.system.common.Constants.COURSE_REVIEW_NAVIGATION
import student.testing.system.models.CourseResponse
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CourseReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @Named(COURSE_REVIEW_NAVIGATION) appNavigator: AppNavigator,
) : ViewModel() {

    val course: CourseResponse =
        checkNotNull(savedStateHandle[Destination.CourseReviewScreen.COURSE_KEY])

    val navigationChannel = appNavigator.navigationChannel
}