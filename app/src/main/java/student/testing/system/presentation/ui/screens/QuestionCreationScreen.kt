package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.R
import student.testing.system.common.iTems
import student.testing.system.presentation.ui.activity.ui.theme.Purple700
import student.testing.system.presentation.ui.components.InputDialog
import student.testing.system.presentation.viewmodels.TestCreationViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionCreationScreen(parentViewModel: TestCreationViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val course by parentViewModel.courseFlow.collectAsState()
    val questionStateWrapper by parentViewModel.questionStateWrapper.collectAsState()
    val answers by parentViewModel.answersFlow.collectAsState()
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
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    iTems(answers, key = { it }) { answer ->
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
                                Column(
                                    modifier = Modifier.padding(
                                        vertical = 8.dp,
                                        horizontal = 16.dp
                                    )
                                ) {
                                    Text(
                                        text = answer.answer,
                                        modifier = Modifier
                                            .widthIn(min = 60.dp)
                                            .clip(CircleShape),
                                        fontSize = 14.sp,
                                        color = Purple700
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showAnswerAddingCreatingDialog) {
            InputDialog(
                titleResId = R.string.answer_adding,
                hintResId = R.string.input_answer,
                positiveButtonResId = R.string.create,
                isError = showAnswerFieldError,
                errorText = R.string.error_empty_field,
                onReceiveListener = questionStateWrapper,
                onDismiss = {
                    showAnswerAddingCreatingDialog = false
                    showAnswerFieldError = false
                }
            ) {
                val isSuccess = parentViewModel.addAnswer(it)
                if (isSuccess) {
                    showAnswerAddingCreatingDialog = false
                    showAnswerFieldError = false
                } else showAnswerFieldError = true
            }
        }
    }
}