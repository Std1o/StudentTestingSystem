package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import student.testing.system.R
import student.testing.system.models.Test
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.MediumButton
import student.testing.system.presentation.viewmodels.TestPassingViewModel

@Composable
fun TestPassingScreen(test: Test, isUserModerator: Boolean) {
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel = hiltViewModel<TestPassingViewModel>()
    viewModel.setInitialData(test, isUserModerator)
    val contentState by viewModel.contentState.collectAsState()
    Surface {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { contentPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                CenteredColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    Text(text = "Прохождение теста ${contentState.question} answers: ${contentState.answers}")
                }
                MediumButton(
                    text = R.string.next,
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) { viewModel.onNextQuestion() }
            }
        }
    }
}