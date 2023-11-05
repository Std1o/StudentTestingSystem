package student.testing.system.presentation.ui.screens.participants

import androidx.compose.material.Surface
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.models.Participant
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
            @Suppress("UNCHECKED_CAST")
            parentViewModel.setCourse(course.copy(participants = data as List<Participant>))
        }
    }
    LastOperationStateUIHandler(
        lastOperationState,
        { viewModel.onErrorReceived() },
        snackbarHostState
    )
}