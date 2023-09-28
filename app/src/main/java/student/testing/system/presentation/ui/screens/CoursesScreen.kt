package student.testing.system.presentation.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import student.testing.system.R
import student.testing.system.annotations.NotScreenState
import student.testing.system.common.AccountSession
import student.testing.system.common.Constants
import student.testing.system.domain.states.RequestState
import student.testing.system.presentation.viewmodels.CoursesViewModel

@OptIn(NotScreenState::class)
@Composable
fun CoursesScreen() {
    val viewModel = hiltViewModel<CoursesViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    var showUserInfoDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    Surface {
        Column {
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
                                .clickable(onClick = {})
                                .padding(vertical = 10.dp, horizontal = 20.dp)
                        )
                    }
                }
            }
            when (val courses = uiState.courses) {
                is RequestState.Empty -> {}
                is RequestState.Error -> {}
                RequestState.Loading -> {}
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
                                            shadow = Shadow(Color.Black, Offset(3.0f, 4.95f), 1.0f)
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
        if (showUserInfoDialog) {
            AlertDialogExample { showUserInfoDialog = false }
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
                Text("Confirm")
            }
        },
        containerColor = Color.White
    )
}