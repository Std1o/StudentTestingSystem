package student.testing.system.presentation.ui.screens.participants

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import student.testing.system.R
import student.testing.system.common.AccountSession
import student.testing.system.common.iTems
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.models.Participant
import student.testing.system.presentation.ui.activity.ui.theme.BlueColor
import student.testing.system.presentation.ui.activity.ui.theme.GrayColor
import student.testing.system.presentation.ui.components.Avatar
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.LastOperationStateUIHandler
import student.testing.system.presentation.viewmodels.CourseSharedViewModel
import student.testing.system.presentation.viewmodels.ParticipantsViewModel

@Composable
fun ParticipantsScreen(parentViewModel: CourseSharedViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val course by parentViewModel.courseFlow.collectAsState()
    val viewModel = hiltViewModel<ParticipantsViewModel>()
    val lastOperationState by viewModel.lastOperationState.collectAsState()
    Surface {
        CenteredColumn {
            ParticipantsList(
                hidden = false,
                participants = course.participants,
                currentParticipant = viewModel.getCurrentParticipant(course),
                onAppointModerator = { id, wasModerator ->
                    if (wasModerator) {
                        viewModel.deleteModerator(course.id, id)
                    } else {
                        viewModel.addModerator(course.id, id)
                    }
                },
                onDelete = { viewModel.deleteParticipant(course.id, it) })
        }
    }
    with(lastOperationState) {
        if (this is OperationState.Success) {
            parentViewModel.setCourse(course.copy(participants = data as List<Participant>))
        }
    }
    LastOperationStateUIHandler(
        lastOperationState,
        { viewModel.onErrorReceived() },
        snackbarHostState
    )
}

@Composable
fun ParticipantsList(
    hidden: Boolean,
    participants: List<Participant>,
    currentParticipant: Participant,
    onAppointModerator: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit
) {
    if (hidden) return
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        iTems(participants, key = { it }) { participant ->
            val shape = RoundedCornerShape(5.dp)
            Card(
                elevation = 10.dp,
                shape = shape,
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Avatar(participant.username)
                        Column {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = participant.username,
                                        fontSize = 17.sp,
                                        color = Color.DarkGray,
                                    )
                                    if (participant.isOwner || participant.isModerator) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                                            tint = if (participant.isOwner) BlueColor else GrayColor,
                                            contentDescription = "",
                                            modifier = Modifier
                                                .padding(start = 3.dp)
                                                .size(12.dp)
                                        )
                                    }
                                }
                                if (currentParticipant.isOwner && !participant.isOwner) {
                                    ParticipantContextMenu(
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd),
                                        onAppointModerator = {
                                            onAppointModerator(
                                                participant.userId,
                                                participant.isModerator
                                            )
                                        },
                                        onDelete = { onDelete(participant.userId) }
                                    )
                                }
                            }
                            Row {
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = participant.email,
                                    modifier = Modifier
                                        .widthIn(min = 100.dp),
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}