package student.testing.system.presentation.ui.screens

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import student.testing.system.models.Test
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.viewmodels.CourseReviewViewModel
import student.testing.system.presentation.viewmodels.TestPassingViewModel

@Composable
fun TestPassingScreen(test: Test, isUserModerator: Boolean) {
    val viewModel = hiltViewModel<TestPassingViewModel>()
    viewModel.setInitialData(test, isUserModerator)
    val contentState by viewModel.contentState.collectAsState()
    Surface {
        CenteredColumn {
            Text(text = "Прохождение теста ${contentState.question} answers: ${contentState.answers}")
        }
    }
}