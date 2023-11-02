package student.testing.system.presentation.ui.screens.testPassing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.models.Test
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.MediumButton
import student.testing.system.presentation.ui.screens.questionCreation.AnswersList
import student.testing.system.presentation.viewmodels.TestPassingViewModel

@Composable
fun TestPassingScreen(test: Test, isUserModerator: Boolean) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
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
                    Text(
                        text = contentState.question,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(30.dp)
                    )
                    AnswersList(answers = contentState.answers)
                }
                CenteredColumn(modifier = Modifier.align(Alignment.BottomCenter)) {
                    Text(
                        text = stringResource(
                            R.string.question_number,
                            (contentState.position + 1),
                            test.questions.size
                        ),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    MediumButton(text = R.string.next) { viewModel.onNextQuestion() }
                }
            }
        }
    }
    LaunchedEffect(key1 = Unit) {
        scope.launch {
            viewModel.snackbarFlow.collect {
                snackbarHostState.showSnackbar(context.resources.getString(it))
            }
        }
    }
}