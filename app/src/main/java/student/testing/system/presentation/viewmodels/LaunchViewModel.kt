package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import student.testing.system.presentation.navigation.AppNavigator
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    appNavigator: AppNavigator
) : ViewModel() {
    val navigationChannel = appNavigator.navigationChannel
}