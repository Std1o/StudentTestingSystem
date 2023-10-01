package student.testing.system.presentation.ui.screens

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import student.testing.system.models.CourseResponse
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.viewmodels.CourseSharedViewModel

@Composable
fun ParticipantsScreen(parentViewModel: CourseSharedViewModel) {
    val course = parentViewModel.courseFlow.collectAsState(CourseResponse("", 0, "", "", listOf()))
    Surface {
        CenteredColumn {
            Text(text = "Участники $course")
        }
    }
}