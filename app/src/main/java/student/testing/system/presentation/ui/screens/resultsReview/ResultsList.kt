package student.testing.system.presentation.ui.screens.resultsReview

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.common.iTems
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.models.ParticipantResult
import student.testing.system.models.ParticipantsResults
import student.testing.system.presentation.ui.components.Avatar
import student.testing.system.presentation.ui.components.Shimmer
import student.testing.system.presentation.ui.components.modifiers.placeholder

@Composable
fun ResultsList(
    isLoading: Boolean,
    hidden: Boolean,
    results: LoadableData<ParticipantsResults>
) {
    if (hidden) return
    val data = (results as? LoadableData.Success)?.data?.results
    val mockTests = listOf(
        ParticipantResult(username = "1"),
        ParticipantResult(username = "2"),
        ParticipantResult(username = "3")
    )
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        iTems(data ?: mockTests) { participantResult ->
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
                        Avatar(participantResult.username)
                        Text(
                            text = participantResult.username,
                            modifier = Modifier
                                .widthIn(min = 60.dp)
                                .clip(CircleShape)
                                .placeholder(isLoading, Shimmer()),
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                        )
                    }
                    Text(
                        text = participantResult.email,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clip(CircleShape)
                            .placeholder(isLoading, Shimmer())
                            .widthIn(min = 100.dp),
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}