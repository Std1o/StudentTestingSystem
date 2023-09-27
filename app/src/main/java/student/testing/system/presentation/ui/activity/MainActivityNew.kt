package student.testing.system.presentation.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.presentation.ui.activity.ui.theme.StudentTestingSystemTheme
import student.testing.system.presentation.ui.screens.CourseReviewScreen
import student.testing.system.presentation.ui.screens.CoursesScreen
import student.testing.system.presentation.ui.screens.LaunchScreen

@AndroidEntryPoint
class MainActivityNew : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentTestingSystemTheme {
                CoursesScreen()
            }
        }
    }
}