package student.testing.system.presentation.ui.screens.courses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.domain.operationTypes.CourseAddingOperations
import student.testing.system.domain.states.ValidatableOperationState
import student.testing.system.models.CourseResponse
import student.testing.system.presentation.ui.components.InputDialog
import student.testing.system.presentation.ui.stateWrapper.StateWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseAddingDialogs(
    showBottomSheet: Boolean,
    lastValidationStateWrapper: StateWrapper<ValidatableOperationState<CourseResponse>>,
    onCreateCourse: (String) -> Unit,
    onJoinCourse: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState: SheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var showCourseCreatingDialog by remember { mutableStateOf(false) }
    var showCourseJoiningDialog by remember { mutableStateOf(false) }

    if (showCourseCreatingDialog) {
        val error = lastValidationStateWrapper.run {
            (uiState as? ValidatableOperationState.ValidationError)
                ?.let {
                    if (it.operationType == CourseAddingOperations.CREATE_COURSE) it.messageResId else 0
                } ?: 0
        }
        InputDialog(
            titleResId = R.string.create_course,
            hintResId = R.string.input_name,
            positiveButtonResId = R.string.create,
            isError = error != 0,
            errorText = error,
            onReceiveListener = lastValidationStateWrapper,
            onDismiss = {
                showCourseCreatingDialog = false
                lastValidationStateWrapper.onReceive()
            }
        ) {
            onCreateCourse(it)
        }
    }

    if (showCourseJoiningDialog) {
        val error = lastValidationStateWrapper.run {
            (uiState as? ValidatableOperationState.ValidationError)
                ?.let {
                    if (it.operationType == CourseAddingOperations.JOIN_COURSE) it.messageResId else 0
                } ?: 0
        }
        InputDialog(
            titleResId = R.string.join_course,
            hintResId = R.string.course_code_hint,
            positiveButtonResId = R.string.btn_continue,
            capitalization = KeyboardCapitalization.Characters,
            isError = error != 0,
            errorText = error,
            onReceiveListener = lastValidationStateWrapper,
            onDismiss = {
                showCourseJoiningDialog = false
                lastValidationStateWrapper.onReceive()
            },
        ) {
            onJoinCourse(it)
        }
    }

    with(lastValidationStateWrapper) {
        (uiState as? ValidatableOperationState.SuccessfulValidation)?.let {
            when (it.operationType) {
                CourseAddingOperations.CREATE_COURSE -> showCourseCreatingDialog = false
                CourseAddingOperations.JOIN_COURSE -> showCourseJoiningDialog = false
                else -> {}
            }
            lastValidationStateWrapper.onReceive()
        }
    }

    if (!showBottomSheet) return

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        val onClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) onDismissRequest()
            }
        }
        Text(
            stringResource(R.string.create_course),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showCourseCreatingDialog = true
                    onClick()
                }
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
        Text(
            stringResource(R.string.join_course),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .clickable {
                    showCourseJoiningDialog = true
                    onClick()
                }
                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
        )
    }
}