package student.testing.system.presentation.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.presentation.ui.activity.ui.theme.StudentTestingSystemTheme
import student.testing.system.presentation.ui.screens.LaunchScreen
import student.testing.system.presentation.ui.screens.LoginScreen

@AndroidEntryPoint
class LaunchActivityNew : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentTestingSystemTheme {
                LaunchScreen()
            }
        }
    }
}