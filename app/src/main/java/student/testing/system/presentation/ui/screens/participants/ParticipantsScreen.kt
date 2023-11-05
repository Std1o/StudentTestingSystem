package student.testing.system.presentation.ui.screens.participants

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.R
import student.testing.system.common.iTems
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.models.CourseResponse
import student.testing.system.models.Participant
import student.testing.system.presentation.ui.components.Avatar
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.Shimmer
import student.testing.system.presentation.ui.components.modifiers.placeholder
import student.testing.system.presentation.ui.screens.courses.CoursesContextMenu
import student.testing.system.presentation.viewmodels.CourseSharedViewModel

@Composable
fun ParticipantsScreen(parentViewModel: CourseSharedViewModel) {
    val course by parentViewModel.courseFlow.collectAsState(CourseResponse("", 0, "", "", listOf()))
    Surface {
        CenteredColumn {
            ParticipantsList(hidden = false, participants = course.participants)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ParticipantsList(
    hidden: Boolean,
    participants: List<Participant>
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
                                Text(
                                    text = participant.username,
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .widthIn(min = 60.dp),
                                    fontSize = 17.sp,
                                    color = Color.DarkGray,
                                )
                                if (!participant.isOwner) {
                                    ParticipantContextMenu(
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd),
                                        onAppointModerator = { },
                                        onDelete = { }
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