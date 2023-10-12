package student.testing.system.presentation.ui.screens

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.annotations.NotScreenState
import student.testing.system.common.AccountSession
import student.testing.system.common.iTems
import student.testing.system.domain.states.LoadableData
import student.testing.system.models.Test
import student.testing.system.presentation.ui.activity.ui.theme.Purple700
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.ConfirmationDialog
import student.testing.system.presentation.ui.components.LastOperationStateUIHandler
import student.testing.system.presentation.ui.components.Shimmer
import student.testing.system.presentation.ui.components.modifiers.placeholder
import student.testing.system.presentation.ui.fragments.TestsFragment
import student.testing.system.presentation.viewmodels.CourseSharedViewModel
import student.testing.system.presentation.viewmodels.TestsViewModel

@Composable
fun TestsScreen(parentViewModel: CourseSharedViewModel) {
    val testsVM = hiltViewModel<TestsViewModel>()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val lastOperationStateWrapper by testsVM.lastOperationStateWrapper.collectAsState()
    val course by parentViewModel.courseFlow.collectAsState()
    testsVM.courseId = course.id
    val contentState by testsVM.contentState.collectAsState()
    val context = LocalContext.current as? Activity
    val currentParticipant = course.participants
        .first { it.userId == AccountSession.instance.userId }
    val isUserModerator = currentParticipant.isModerator || currentParticipant.isOwner
    var deletingTestId by remember { mutableStateOf<Int?>(null) }

    Surface {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                if (isUserModerator) {
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
                val courseCodeWasCopied = stringResource(R.string.course_code_copied)
                Text(
                    text = stringResource(R.string.course_code, course.courseCode),
                    modifier = Modifier
                        .padding(20.dp)
                        .clickable {
                            val clipboard =
                                context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip =
                                ClipData.newPlainText(TestsFragment.COURSE_CODE, course.courseCode);
                            clipboard.setPrimaryClip(clip)
                            scope.launch {
                                snackbarHostState.showSnackbar(courseCodeWasCopied)
                            }
                        },
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
                TestsList(
                    isLoading = contentState.tests is LoadableData.Loading,
                    hidden = false,
                    tests = contentState.tests,
                    onClick = {},
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

@OptIn(NotScreenState::class, ExperimentalFoundationApi::class)
@Composable
fun TestsList(
    isLoading: Boolean,
    hidden: Boolean,
    tests: LoadableData<List<Test>>,
    onClick: (Test) -> Unit,
    onLongClick: (Test) -> Unit
) {
    if (hidden) return
    val data = (tests as? LoadableData.Success)?.data
    val mockTests = listOf(Test(id = 0), Test(id = 1), Test(id = 2))
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        iTems(data ?: mockTests, key = { it }) { test ->
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
                            onClick = { onClick(test) },
                            onLongClick = { onLongClick(test) },
                        )
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
                        Text(
                            text = test.name,
                            modifier = Modifier
                                .widthIn(min = 60.dp)
                                .clip(CircleShape)
                                .placeholder(isLoading, Shimmer()),
                            fontSize = 14.sp,
                            color = Purple700
                        )
                        Text(
                            text = test.creationTime,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .clip(CircleShape)
                                .widthIn(min = 100.dp)
                                .placeholder(isLoading, Shimmer()),
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}