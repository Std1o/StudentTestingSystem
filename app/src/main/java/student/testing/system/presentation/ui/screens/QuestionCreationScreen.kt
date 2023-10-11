package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import student.testing.system.R
import student.testing.system.domain.operationTypes.CourseAddingOperations
import student.testing.system.domain.states.ValidatableOperationState
import student.testing.system.models.CourseResponse
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.InputDialog
import student.testing.system.presentation.viewmodels.CourseSharedViewModel
import student.testing.system.presentation.viewmodels.TestCreationViewModel

@Composable
fun QuestionCreationScreen(parentViewModel: TestCreationViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val course by parentViewModel.courseFlow.collectAsState()
    var showAnswerAddingCreatingDialog by remember { mutableStateOf(false) }
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
            }
        }

        if (showAnswerAddingCreatingDialog) {
//            val error = lastValidationStateWrapper.run {
//                (uiState as? ValidatableOperationState.ValidationError)
//                    ?.let {
//                        if (it.operationType == CourseAddingOperations.CREATE_COURSE) it.messageResId else 0
//                    } ?: 0
//            }
            InputDialog(
                titleResId = R.string.answer_adding,
                hintResId = R.string.input_answer,
                positiveButtonResId = R.string.create,
                isError = /*error != 0*/ false,
                errorText = /*error*/ 0,
                onReceiveListener = /*lastValidationStateWrapper*/ null,
                onDismiss = { showAnswerAddingCreatingDialog = false }
            ) {
                //parentViewModel.addQuestion()
            }
        }
    }
}