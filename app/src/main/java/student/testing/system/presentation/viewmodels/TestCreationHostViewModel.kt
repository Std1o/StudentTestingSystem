package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import student.testing.system.common.CourseReviewNavigation
import student.testing.system.common.TestCreationNavigation
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class TestCreationHostViewModel @Inject constructor(
    @TestCreationNavigation testCreationNavigator: AppNavigator,
    @CourseReviewNavigation private val courseReviewNavigator: AppNavigator
) : ViewModel() {

    val navigationChannel = testCreationNavigator.navigationChannel

    fun navigateBack() {
        courseReviewNavigator.tryNavigateBack(Destination.TestsScreen.fullRoute)
    }
}