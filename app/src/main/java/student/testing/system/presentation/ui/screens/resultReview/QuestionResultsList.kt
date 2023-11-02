package student.testing.system.presentation.ui.screens.resultReview

import androidx.compose.foundation.ExperimentalFoundationApi
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
import student.testing.system.models.QuestionResult
import student.testing.system.presentation.ui.components.CenteredColumn

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionResultsList(questionResults: List<QuestionResult>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp)
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
                        modifier = Modifier.padding(start = 10.dp),
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    AnswerResultsList(answerResults = answers)
                }
            }
        }
    }
}