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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.models.Test
import student.testing.system.domain.models.TestResult
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.ConfirmationDialog
import student.testing.system.presentation.ui.components.OptionsDialog
import student.testing.system.presentation.ui.components.UIReactionOnLastOperationState
import student.testing.system.presentation.ui.components.UIReactionOnListState
import student.testing.system.presentation.viewmodels.CourseSharedViewModel
import student.testing.system.presentation.viewmodels.TestsViewModel

@Composable
fun TestsScreen(
    parentViewModel: CourseSharedViewModel,
    onTestClicked: (Test) -> Unit,
    onResultReview: (TestResult) -> Unit
) {
    val testsVM = hiltViewModel<TestsViewModel>()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val lastOperationState by testsVM.lastOperationState.collectAsState()
    val course by parentViewModel.courseFlow.collectAsState()
    val events by testsVM.events.collectAsState(OperationState.NoState)
    testsVM.course = course
    testsVM.courseId = course.id
    val contentState by testsVM.contentState.collectAsState()
    var deletingTestId by remember { mutableStateOf<Int?>(null) }
    val options = listOf(context.getString(R.string.check), context.getString(R.string.delete))
    var selectedTest by remember { mutableStateOf<Test?>(null) }

    Surface {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                if (testsVM.isUserAModerator) {
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
                var hideTestsList by remember { mutableStateOf(false) }
                TestsList(
                    isLoading = contentState.tests is LoadableData.Loading,
                    hidden = hideTestsList,
                    tests = contentState.tests,
                    onClick = {
                        onTestClicked(it)
                        testsVM.onTestClicked(it)
                    },
                    onLongClick = { if (testsVM.isUserAModerator) selectedTest = it else null })
                UIReactionOnListState(
                    loadableData = contentState.tests,
                    onRetry = { testsVM.getTests() },
                    emptyListText = R.string.empty_tests
                ) {
                    hideTestsList = it
                }
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
    selectedTest?.let { test ->
        OptionsDialog(options = options, onDismiss = { selectedTest = null }) {
            when (it) {
                0 -> {
                    onTestClicked(test)
                    testsVM.onCheckOptionSelected()
                }

                1 -> deletingTestId = test.id
            }
        }
    }
    LaunchedEffect(key1 = Unit) {
        scope.launch {
            parentViewModel.testFlow.collect {
                testsVM.onTestAdded(it)
            }
        }
        scope.launch {
            testsVM.resultReviewFlow.collect {
                onResultReview(it)
            }
        }
    }
    UIReactionOnLastOperationState(lastOperationState, events, snackbarHostState)
}