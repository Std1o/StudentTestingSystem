package student.testing.system.presentation.ui.screens.tests

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.common.Constants
import student.testing.system.domain.models.CourseResponse

@Composable
fun CourseCode(
    course: CourseResponse,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current as? Activity
    val courseCodeWasCopied = stringResource(R.string.course_code_copied)
    Text(
        text = stringResource(R.string.course_code, course.courseCode),
        modifier = Modifier
            .padding(20.dp)
            .clickable {
                val clipboard =
                    context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip =
                    ClipData.newPlainText(Constants.COURSE_CODE, course.courseCode);
                clipboard.setPrimaryClip(clip)
                scope.launch {
                    snackbarHostState.showSnackbar(courseCodeWasCopied)
                }
            },
        textAlign = TextAlign.Center,
        color = Color.Black
    )
}