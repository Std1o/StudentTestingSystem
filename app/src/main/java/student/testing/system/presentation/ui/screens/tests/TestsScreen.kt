package student.testing.system.presentation.ui.screens.tests

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.domain.states.LoadableData
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.ConfirmationDialog
import student.testing.system.presentation.ui.components.LastOperationStateUIHandler
import student.testing.system.presentation.viewmodels.CourseSharedViewModel
import student.testing.system.presentation.viewmodels.TestsViewModel

@Composable
fun TestsScreen(parentViewModel: CourseSharedViewModel) {
    val testsVM = hiltViewModel<TestsViewModel>()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val lastOperationStateWrapper by testsVM.lastOperationStateWrapper.collectAsState()
    val course by parentViewModel.courseFlow.collectAsState()
    testsVM.course = course
    testsVM.courseId = course.id
    val contentState by testsVM.contentState.collectAsState()
    var deletingTestId by remember { mutableStateOf<Int?>(null) }

    Surface {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                if (testsVM.isUserModerator) {
                    FloatingActionButton(
                        onClick = {
                            testsVM.navigateToTestCreation(course)
                        },
                        shape = CircleShape,
                        backgroundColor = Color.White,
                        modifier = Modifier.padding(bottom = 10.dp, end = 4.dp)
                    ) {
                        Icon(Icons.Filled.Add, "")
                    }
                }
            }
        ) { contentPadding ->
            CenteredColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                CourseCode(course = course, scope = scope, snackbarHostState = snackbarHostState)
                TestsList(
                    isLoading = contentState.tests is LoadableData.Loading,
                    hidden = false,
                    tests = contentState.tests,
                    onClick = { testsVM.onTestClicked(it) },
                    onLongClick = { deletingTestId = it.id })
            }
        }
        deletingTestId?.let {
            ConfirmationDialog(
                onDismissRequest = { deletingTestId = null },
                onConfirmation = {
                    testsVM.deleteTest(testId = it, courseId = course.id)
                    deletingTestId = null
                },
                dialogText = stringResource(id = R.string.delete_request)
            )
        }
    }
    LaunchedEffect(key1 = Unit) {
        scope.launch {
            parentViewModel.testFlow.collect {
                testsVM.onTestAdded(it)
            }
        }
    }
    LastOperationStateUIHandler(lastOperationStateWrapper, snackbarHostState)
}