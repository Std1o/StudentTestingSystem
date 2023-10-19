package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.common.iTems
import student.testing.system.models.CourseResponse
import student.testing.system.models.Participant
import student.testing.system.presentation.ui.components.Avatar
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.viewmodels.CourseSharedViewModel

@Composable
fun ParticipantsScreen(parentViewModel: CourseSharedViewModel) {
    val course by parentViewModel.courseFlow.collectAsState(CourseResponse("", 0, "", "", listOf()))
    Surface {
        CenteredColumn {
            ParticipantsList(hidden = false, participants = course.participants, onClick = {})
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ParticipantsList(
    hidden: Boolean,
    participants: List<Participant>,
    onClick: (Participant) -> Unit
) {
    if (hidden) return
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        iTems(participants, key = { it }) { participant ->
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
                        .clickable { onClick(participant) }
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Avatar(participant.username)
                            Text(
                                text = participant.username,
                                modifier = Modifier
                                    .widthIn(min = 60.dp)
                                    .clip(CircleShape),
                                fontSize = 16.sp,
                                color = Color.DarkGray,
                            )
                        }
                        Text(
                            text = participant.email,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .clip(CircleShape)
                                .widthIn(min = 100.dp),
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}