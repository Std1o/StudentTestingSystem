package student.testing.system.presentation.ui.screens

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import student.testing.system.models.CourseResponse
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.viewmodels.CourseSharedViewModel
import student.testing.system.presentation.viewmodels.TestCreationViewModel

@Composable
fun TestCreationScreen(parentViewModel: TestCreationViewModel) {
    val course by parentViewModel.courseFlow.collectAsState()
    Surface {
        CenteredColumn {
            Text(text = "Создание тестааф ${course.participants}")
        }
    }
}