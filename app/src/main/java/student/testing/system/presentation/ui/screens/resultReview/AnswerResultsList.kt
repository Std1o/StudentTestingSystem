package student.testing.system.presentation.ui.screens.resultReview

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.models.AnswerResult

@Composable
fun AnswerResultsList(answerResults: List<AnswerResult>) {
    answerResults.forEach { answerResult ->
        val shape = RoundedCornerShape(5.dp)
        Card(
            elevation = 10.dp,
            shape = shape,
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Checkbox(checked = answerResult.isSelected, onCheckedChange = null, enabled = false)
                Text(
                    text = answerResult.answer, modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f),
                    fontSize = 14.sp,
                    color = if (answerResult.isRight) Color.Green else Color.Red
                )
            }
        }
    }
}