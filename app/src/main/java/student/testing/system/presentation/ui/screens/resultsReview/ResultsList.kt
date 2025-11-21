package student.testing.system.presentation.ui.screens.resultsReview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import student.testing.system.domain.models.ParticipantResult
import student.testing.system.domain.models.ParticipantsResults
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.presentation.ui.components.Avatar
import student.testing.system.presentation.ui.components.Shimmer
import student.testing.system.presentation.ui.components.modifiers.placeholder

@Composable
fun ResultsList(
    isLoading: Boolean,
    hidden: Boolean,
    results: LoadableData<ParticipantsResults>,
    onResultClicked: (ParticipantResult) -> Unit
) {
    if (hidden) return
    val data = (results as? LoadableData.Success)?.data
    val mockTests = listOf(
        ParticipantResult(userId = 0),
        ParticipantResult(userId = 1),
        ParticipantResult(userId = 2)
    )
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        iTems(data?.results ?: mockTests, key = { it.userId }) { participantResult ->
            val shape = RoundedCornerShape(5.dp)
            Card(
                elevation = 10.dp,
                shape = shape,
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 16.dp)
                    .fillMaxWidth()
                    .clickable {
                        onResultClicked(participantResult)
                    }
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isLoading) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Spacer(modifier = Modifier.height(6.dp))
                                Spacer(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Shimmer())
                                        .padding(10.dp)
                                        .size(22.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                        } else if (results is LoadableData.Success) {
                            Avatar(participantResult.username)
                        }
                        Column {
                            Text(
                                text = participantResult.username,
                                modifier = Modifier
                                    .widthIn(min = 60.dp)
                                    .clip(if (isLoading) CircleShape else RectangleShape)
                                    .placeholder(isLoading, Shimmer()),
                                fontSize = 17.sp,
                                color = Color.DarkGray,
                            )
                            val score =
                                if (data == null) "" else if (participantResult.score % 1.0 != 0.0) {
                                    stringResource(
                                        R.string.participant_result,
                                        participantResult.score,
                                        data.maxScore
                                    )
                                } else {
                                    stringResource(
                                        R.string.participant_int_result,
                                        participantResult.score.toInt(),
                                        data.maxScore
                                    )
                                }
                            Row {
                                Text(
                                    text = score,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .widthIn(min = 40.dp)
                                        .padding(top = if (isLoading) 3.dp else 0.dp)
                                        .clip(if (isLoading) CircleShape else RectangleShape)
                                        .placeholder(isLoading, Shimmer()),
                                    color = Color.DarkGray,
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = participantResult.email,
                                    modifier = Modifier
                                        .clip(if (isLoading) CircleShape else RectangleShape)
                                        .placeholder(isLoading, Shimmer())
                                        .widthIn(min = 100.dp),
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                    Divider(
                        color = Color.DarkGray,
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Text(
                        text = if (participantResult.passingTime.isEmpty()) "" else stringResource(
                            R.string.passing_time,
                            participantResult.passingTime
                        ),
                        modifier = Modifier
                            .clip(if (isLoading) CircleShape else RectangleShape)
                            .placeholder(isLoading, Shimmer())
                            .widthIn(min = 200.dp),
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}