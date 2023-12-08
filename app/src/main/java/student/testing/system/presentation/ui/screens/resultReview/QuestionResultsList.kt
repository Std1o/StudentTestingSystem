package student.testing.system.presentation.ui.screens.resultReview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.common.iTems
import student.testing.system.domain.models.QuestionResult
import student.testing.system.presentation.ui.components.CenteredColumn

@Composable
fun QuestionResultsList(questionResults: List<QuestionResult>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        iTems(questionResults, key = { it }) { (question, answers) ->
            val shape = RoundedCornerShape(5.dp)
            Card(
                elevation = 10.dp,
                shape = shape,
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                CenteredColumn(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = question,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    AnswerResultsList(answerResults = answers)
                }
            }
        }
    }
}