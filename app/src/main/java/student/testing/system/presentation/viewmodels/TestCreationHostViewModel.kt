package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import student.testing.system.common.Constants.COURSE_REVIEW_NAVIGATION
import student.testing.system.common.Constants.TEST_CREATION_NAVIGATION
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TestCreationHostViewModel @Inject constructor(
    @Named(TEST_CREATION_NAVIGATION) testCreationNavigator: AppNavigator,
    @Named(COURSE_REVIEW_NAVIGATION) private val courseReviewNavigator: AppNavigator
) : ViewModel() {

    val navigationChannel = testCreationNavigator.navigationChannel

    fun navigateBack() {
        courseReviewNavigator.tryNavigateBack(Destination.TestsScreen.fullRoute)
    }
}