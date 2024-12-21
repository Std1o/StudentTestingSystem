package student.testing.system.presentation.ui.screens.resultReview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.R
import student.testing.system.domain.models.TestResult
import student.testing.system.presentation.ui.components.CenteredColumn

@Composable
fun ResultReviewScreen(testResult: TestResult) {
    val savedTestResult = rememberSaveable { testResult}
    Surface {
        CenteredColumn {
            val score = if (savedTestResult.score % 1.0 != 0.0) {
                stringResource(
                    R.string.total_user_result,
                    savedTestResult.score,
                    savedTestResult.maxScore
                )
            } else {
                stringResource(
                    R.string.total_user_result_int,
                    savedTestResult.score.toInt(),
                    savedTestResult.maxScore
                )
            }
            Text(
                text = score,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(30.dp)
            )
            QuestionResultsList(questionResults = savedTestResult.questions)
        }
    }
}