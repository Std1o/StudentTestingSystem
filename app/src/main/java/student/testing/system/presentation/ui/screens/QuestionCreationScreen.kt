package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.R
import student.testing.system.common.iTems
import student.testing.system.domain.addQuestion.QuestionState
import student.testing.system.presentation.ui.components.InputDialog
import student.testing.system.presentation.ui.components.requiredTextField
import student.testing.system.presentation.viewmodels.TestCreationViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionCreationScreen(parentViewModel: TestCreationViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val course by parentViewModel.courseFlow.collectAsState()
    val questionStateWrapper by parentViewModel.questionStateWrapper.collectAsState()
    val contentState = parentViewModel.questionCreationContentState
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
                    modifier = Modifier.padding(bottom = 10.dp, end = 4.dp)
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                Text(text = "Создание вопроса: ${course.courseCode}")
                val question = requiredTextField(
                    onReceiveListener = questionStateWrapper,
                    contentState = contentState.questionContentState,
                    isError = questionStateWrapper.uiState is QuestionState.EmptyQuestion,
                    errorText = R.string.error_empty_field,
                    hint = R.string.input_question
                )
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    iTems(contentState.answers, key = { it }) { answer ->
                        val shape = RoundedCornerShape(5.dp)
                        Card(
                            elevation = 10.dp,
                            shape = shape,
                            modifier = Modifier
                                .animateItemPlacement()
                                .padding(vertical = 10.dp, horizontal = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(shape)
                                    .combinedClickable(
                                        onClick = { },
                                        onLongClick = { },
                                    )
                            ) {
                                var checked by remember { mutableStateOf(answer.isRight) }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .toggleable(
                                            value = checked,
                                            role = Role.Checkbox,
                                            onValueChange = {
                                                checked = !checked
                                                answer.isRight = checked
                                            }
                                        )
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                ) {
                                    Checkbox(
                                        checked = checked,
                                        onCheckedChange = { isRight ->
                                            answer.isRight = isRight
                                        })
                                    Text(
                                        text = answer.answer, modifier = Modifier
                                            .padding(start = 10.dp)
                                            .weight(1f),
                                        fontSize = 14.sp,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                }
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