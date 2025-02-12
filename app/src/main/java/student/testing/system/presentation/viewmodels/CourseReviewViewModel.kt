package student.testing.system.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import student.testing.system.common.CourseReviewNavigation
import student.testing.system.common.Utils.isUserAModerator
import student.testing.system.domain.models.CourseResponse
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class CourseReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @CourseReviewNavigation appNavigator: AppNavigator,
) : ViewModel() {

    val course: CourseResponse =
        checkNotNull(savedStateHandle[Destination.CourseReviewScreen.COURSE_KEY])

    val navigationChannel = appNavigator.navigationChannel

    val isUserAModerator by lazy {
        isUserAModerator(course)
    }
}