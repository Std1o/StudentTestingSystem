package student.testing.system.presentation.ui.screens.testCreation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.R
import student.testing.system.common.iTems
import student.testing.system.domain.states.TestCreationState
import student.testing.system.presentation.ui.activity.ui.theme.Purple700
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.LastOperationStateUIHandler
import student.testing.system.presentation.ui.components.requiredTextField
import student.testing.system.presentation.viewmodels.TestCreationViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestCreationScreen(parentViewModel: TestCreationViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val lastOperationStateWrapper by parentViewModel.lastOperationStateWrapper.collectAsState()
    val course by parentViewModel.courseFlow.collectAsState()
    val testStateWrapper by parentViewModel.testStateWrapper.collectAsState()
    val screenSession = parentViewModel.testCreationScreenSession
    Surface {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { parentViewModel.navigateToQuestionCreation() },
                    shape = CircleShape,
                    backgroundColor = Color.White,
                    modifier = Modifier.padding(bottom = 60.dp, end = 4.dp)
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            }
        ) { contentPadding ->
            lateinit var testName: String
            Box(modifier = Modifier.fillMaxSize()) {
                CenteredColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    testName = requiredTextField(
                        modifier = Modifier.padding(top = 30.dp),
                        onReceiveListener = testStateWrapper,
                        fieldState = screenSession.testNameState,
                        isError = testStateWrapper.uiState is TestCreationState.EmptyName,
                        errorText = R.string.error_empty_field,
                        hint = R.string.test_name,
                    )
                    Text(text = stringResource(R.string.questions), fontSize = 16.sp)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 70.dp)
                    ) {
                        iTems(screenSession.questions, key = { it }) { question ->
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
                                            onClick = { },
                                            onLongClick = { },
                                        )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(
                                            vertical = 8.dp,
                                            horizontal = 16.dp
                                        )
                                    ) {
                                        Text(
                                            text = question.question,
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            fontSize = 14.sp,
                                            color = Purple700
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        parentViewModel.createTest(course.id)
                    }, modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp)
                        .height(45.dp)
                        .width(200.dp)
                ) { androidx.compose.material3.Text(stringResource(R.string.publish)) }
            }
        }
    }
    LastOperationStateUIHandler(lastOperationStateWrapper, snackbarHostState)
}