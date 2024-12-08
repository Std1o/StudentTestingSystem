package student.testing.system.presentation.ui.screens.courses

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import student.testing.system.R
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.presentation.ui.activity.LaunchActivity
import student.testing.system.presentation.ui.components.ConfirmationDialog
import student.testing.system.presentation.ui.components.UIReactionOnListState
import student.testing.system.presentation.ui.components.UIReactionOnLastOperationState
import student.testing.system.presentation.viewmodels.CoursesViewModel

@Composable
fun CoursesScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel = hiltViewModel<CoursesViewModel>()
    val contentState by viewModel.contentState.collectAsState()
    val lastOperationState by viewModel.lastOperationState.collectAsState()
    val coursesState by viewModel.coursesState.collectAsState()
    var showUserInfoDialog by rememberSaveable { mutableStateOf(false) }
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    var deletingCourseId by remember { mutableStateOf<Int?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    if (contentState.isLoggedOut) { // TODO check comment in CoursesContentState
        val activity = (LocalContext.current as? Activity)
        activity?.finish()
        activity?.startActivity(Intent(activity, LaunchActivity::class.java))
    }
    Surface {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showBottomSheet = true },
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.courses),
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    CoursesContextMenu(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 20.dp),
                        onUserInfoDialogShow = { showUserInfoDialog = true },
                        onLogoutDialogShow = { showLogoutDialog = true }
                    )
                }
                val coursesIsLoading = contentState.courses is LoadableData.Loading
                var hideCoursesList by remember { mutableStateOf(false) }
                CoursesList(
                    isLoading = coursesIsLoading,
                    hidden = hideCoursesList,
                    courses = contentState.courses, onClick = { viewModel.onCourseClicked(it) },
                    onLongClick = { deletingCourseId = it.id },
                )
                UIReactionOnListState(
                    loadableData = contentState.courses,
                    onRetry = { viewModel.getCourses() },
                    emptyListText = R.string.empty_courses
                ) {
                    hideCoursesList = it
                }
            }
        }

        CourseAddingDialogs(
            showBottomSheet = showBottomSheet,
            coursesState = coursesState,
            onNeedResetValidation = { viewModel.onNeedResetValidation(it) },
            onCreateCourse = { viewModel.createCourse(it) },
            onJoinCourse = { viewModel.joinCourse(it) }
        ) { showBottomSheet = false }

        if (showUserInfoDialog) {
            AlertUserInfoDialog { showUserInfoDialog = false }
        }
        if (showLogoutDialog) {
            ConfirmationDialog(
                onDismissRequest = { showLogoutDialog = false },
                onConfirmation = {
                    showLogoutDialog = false
                    viewModel.logout()
                },
                dialogText = stringResource(id = R.string.logout_request)
            )
        }
        deletingCourseId?.let {
            ConfirmationDialog(
                onDismissRequest = { deletingCourseId = null },
                onConfirmation = {
                    viewModel.deleteCourse(it)
                    deletingCourseId = null
                },
                dialogText = stringResource(id = R.string.delete_request)
            )
        }
    }
    UIReactionOnLastOperationState(
        lastOperationState,
        { viewModel.onErrorReceived() },
        snackbarHostState
    )
}