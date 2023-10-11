package student.testing.system.presentation.ui.screens

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import student.testing.system.models.CourseResponse
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.viewmodels.CourseReviewViewModel
import student.testing.system.presentation.viewmodels.CourseSharedViewModel
import student.testing.system.presentation.viewmodels.TestCreationViewModel

@Composable
fun TestCreationScreen() {
    val viewModel = hiltViewModel<TestCreationViewModel>()
    Surface {
        CenteredColumn {
            Text(text = "Создание теста ${viewModel.course}")
        }
    }
}