package student.testing.system.presentation.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.annotations.NotScreenState
import student.testing.system.common.AccountSession
import student.testing.system.common.Constants
import student.testing.system.domain.states.RequestState
import student.testing.system.presentation.ui.activity.LaunchActivity
import student.testing.system.presentation.ui.components.ConfirmationDialog
import student.testing.system.presentation.ui.components.LoadingIndicator
import student.testing.system.presentation.viewmodels.CoursesViewModel

@OptIn(NotScreenState::class, ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel = hiltViewModel<CoursesViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    var showUserInfoDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val sheetState: SheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }


    if (uiState.isLoggedOut) {
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
                ExtendedFloatingActionButton(
                    text = { Text("Show bottom sheet") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                    onClick = {
                        showBottomSheet = true
                    }
                )
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
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                    var expanded by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 20.dp)
                    ) {
                        IconButton(
                            onClick = { expanded = true },
                        ) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Показать меню")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color.White),
                        ) {
                            Text(
                                stringResource(R.string.who_am_i), modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = {
                                        expanded = false
                                        showUserInfoDialog = true
                                    })
                                    .padding(vertical = 10.dp, horizontal = 20.dp)
                            )
                            Text(
                                stringResource(R.string.logout), modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = {
                                        expanded = false
                                        showConfirmationDialog = true
                                    })
                                    .padding(vertical = 10.dp, horizontal = 20.dp)
                            )
                        }
                    }
                }
                when (val courses = uiState.courses) {
                    is RequestState.Empty -> {}
                    is RequestState.Error -> {}
                    RequestState.Loading -> LoadingIndicator()
                    RequestState.NoState -> {}
                    is RequestState.Success -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            courses.data.forEach { course ->
                                item {
                                    Box(
                                        modifier = Modifier
                                            .padding(vertical = 10.dp, horizontal = 16.dp)
                                            .height(150.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { viewModel.onCourseClicked(course) }
                                    ) {
                                        AsyncImage(
                                            model = "${Constants.BASE_URL}images/${course.img}",
                                            contentDescription = "Translated description of what the image contains",
                                            modifier = Modifier.fillMaxWidth(),
                                            contentScale = ContentScale.FillWidth
                                        )
                                        Text(
                                            text = course.name,
                                            modifier = Modifier.padding(16.dp),
                                            fontSize = 20.sp,
                                            color = Color.White,
                                            style = TextStyle(
                                                shadow = Shadow(
                                                    Color.Black,
                                                    Offset(3.0f, 4.95f),
                                                    1.0f
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    is RequestState.ValidationError -> {}
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                val onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) showBottomSheet = false
                    }
                }
                Text(
                    stringResource(R.string.create_course),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick() }
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
                Text(stringResource(R.string.join_course),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                        .clickable { onClick() }
                        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                )
            }
        }

        if (showUserInfoDialog) {
            AlertDialogExample { showUserInfoDialog = false }
        }
        if (showConfirmationDialog) {
            ConfirmationDialog(
                onDismissRequest = { showConfirmationDialog = false },
                onConfirmation = {
                    showConfirmationDialog = false
                    viewModel.logout()
                },
                dialogText = stringResource(id = R.string.logout_request)
            )
        }
    }
}

@Composable
fun AlertDialogExample(onDismissRequest: () -> Unit) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.we_remind_you))
        },
        text = {
            val account = AccountSession.instance
            Text(
                text = stringResource(
                    R.string.user_info,
                    account.username ?: "",
                    account.email ?: ""
                )
            )
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(stringResource(id = R.string.thanks))
            }
        },
        containerColor = Color.White
    )
}