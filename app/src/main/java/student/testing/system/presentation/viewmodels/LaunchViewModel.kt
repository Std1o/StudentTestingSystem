package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import student.testing.system.presentation.navigation.AppNavigator
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LaunchViewModel @Inject constructor(
    @Named("LaunchNavigation") appNavigator: AppNavigator
) : ViewModel() {
    val navigationChannel = appNavigator.navigationChannel
}