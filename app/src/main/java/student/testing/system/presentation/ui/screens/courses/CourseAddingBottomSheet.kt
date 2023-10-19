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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import student.testing.system.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseAddingBottomSheet(
    showBottomSheet: Boolean,
    onShowCourseCreating: () -> Unit,
    onShowCourseJoining: () -> Unit,
    onDismissRequest: () -> Unit
) {
    if (!showBottomSheet) return
    val sheetState: SheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

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
                    onShowCourseCreating()
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
                    onShowCourseJoining()
                    onClick()
                }
                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
        )
    }
}