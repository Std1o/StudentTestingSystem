package student.testing.system.presentation.ui.screens.questionCreation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.R
import student.testing.system.domain.states.QuestionState
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.presentation.ui.components.*
import student.testing.system.presentation.viewmodels.TestCreationSharedViewModel

@Composable
fun QuestionCreationScreen(parentViewModel: TestCreationSharedViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val questionState by parentViewModel.questionState.collectAsState()
    val screenSession = parentViewModel.questionCreationScreenSession
    var showAnswerAddingDialog by rememberSaveable { mutableStateOf(false) }
    val lastOperationState by parentViewModel.lastOperationState.collectAsState()
    val events by parentViewModel.events.collectAsState(OperationState.NoState)
    Surface {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAnswerAddingDialog = true },
                    shape = CircleShape,
                    backgroundColor = Color.White,
                    modifier = Modifier.padding(bottom = 60.dp, end = 4.dp)
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            }
        ) { contentPadding ->
            lateinit var question: String
            if (questionState is QuestionState.NoCorrectAnswers) {
                val text = stringResource(R.string.error_select_answers)
                LaunchedEffect(Unit) {
                    snackbarHostState.showSnackbar(text)
                    parentViewModel.onQuestionStateReceived()
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                CenteredColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 30.dp, end = 16.dp)
                    ) {
                        RequiredTextField(
                            modifier = Modifier
                                .padding(start = 32.dp, end = 8.dp)
                                .weight(1f),
                            onTextChanged = {
                                parentViewModel.onQuestionStrChange(it)
                                parentViewModel.onQuestionStateReceived()
                            },
                            isError = questionState is QuestionState.EmptyQuestion,
                            errorText = R.string.error_empty_field,
                            hint = R.string.input_question,
                        )
                        IconButton(onClick = { parentViewModel.getAIQuestion() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_ai),
                                contentDescription = "",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Text(text = stringResource(R.string.answers), fontSize = 16.sp)
                    AnswersList(answers = screenSession.answers)
                }
                MediumButton(
                    text = R.string.save,
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) { parentViewModel.addQuestion() }
            }
        }

        if (showAnswerAddingDialog) {
            AnswerAddingDialog(
                onDismiss = { showAnswerAddingDialog = false },
                onAnswerAdded = { parentViewModel.addAnswer(it) }
            )
        }
    }
    UIReactionOnLastOperationState(lastOperationState, events, snackbarHostState, onLoading = {
        LoadingDialog(stringResource(R.string.question_generation_in_progress)) {
            parentViewModel.cancelQuestionGeneration()
        }
    })
}