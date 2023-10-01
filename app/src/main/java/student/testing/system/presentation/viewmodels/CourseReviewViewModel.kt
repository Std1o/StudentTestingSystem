package student.testing.system.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import student.testing.system.models.CourseResponse
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class CourseReviewViewModel @Inject constructor(
    appNavigator: AppNavigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        val course =
            savedStateHandle.get<CourseResponse>(Destination.CourseReviewScreen.COURSE_KEY)
        println("course: $course")
    }
}