package student.testing.system.presentation.ui.screens.questionCreation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.R
import student.testing.system.common.iTems
import student.testing.system.domain.addQuestion.QuestionState
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.InputDialog
import student.testing.system.presentation.ui.components.requiredTextField
import student.testing.system.presentation.viewmodels.TestCreationViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionCreationScreen(parentViewModel: TestCreationViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val questionStateWrapper by parentViewModel.questionStateWrapper.collectAsState()
    val screenSession = parentViewModel.questionCreationScreenSession
    var showAnswerAddingCreatingDialog by remember { mutableStateOf(false) }
    var showAnswerFieldError by remember { mutableStateOf(false) }
    Surface {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAnswerAddingCreatingDialog = true },
                    shape = CircleShape,
                    backgroundColor = Color.White,
                    modifier = Modifier.padding(bottom = 60.dp, end = 4.dp)
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            }
        ) { contentPadding ->
            lateinit var question: String
            if (questionStateWrapper.uiState is QuestionState.NoCorrectAnswers) {
                val text = stringResource(R.string.error_select_answers)
                LaunchedEffect(Unit) {
                    snackbarHostState.showSnackbar(text)
                    questionStateWrapper.onReceive()
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                CenteredColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    question = requiredTextField(
                        modifier = Modifier.padding(top = 30.dp),
                        onReceiveListener = questionStateWrapper,
                        fieldState = screenSession.questionState,
                        isError = questionStateWrapper.uiState is QuestionState.EmptyQuestion,
                        errorText = R.string.error_empty_field,
                        hint = R.string.input_question,
                    )
                    Text(text = stringResource(R.string.answers), fontSize = 16.sp)
                    AnswersList(answers = screenSession.answers)
                }
                Button(
                    onClick = {
                        parentViewModel.addQuestion(question)
                    }, modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp)
                        .height(45.dp)
                        .width(200.dp)
                ) { androidx.compose.material3.Text(stringResource(R.string.save)) }
            }
        }

        var answerError by remember { mutableIntStateOf(0) }
        if (showAnswerAddingCreatingDialog) {
            InputDialog(
                titleResId = R.string.answer_adding,
                hintResId = R.string.input_answer,
                positiveButtonResId = R.string.create,
                isError = showAnswerFieldError,
                errorText = answerError,
                onReceiveListener = questionStateWrapper,
                onDismiss = {
                    showAnswerAddingCreatingDialog = false
                    showAnswerFieldError = false
                }
            ) {
                answerError = parentViewModel.addAnswer(it)
                if (answerError == 0) {
                    showAnswerAddingCreatingDialog = false
                    showAnswerFieldError = false
                } else showAnswerFieldError = true
            }
        }
    }
}