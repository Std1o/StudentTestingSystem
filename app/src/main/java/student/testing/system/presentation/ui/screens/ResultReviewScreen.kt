package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.R
import student.testing.system.models.TestResult
import student.testing.system.presentation.ui.components.CenteredColumn

@Composable
fun ResultReviewScreen(testResult: TestResult) {
    Surface {
        CenteredColumn {
            val score = if (testResult.score % 1.0 != 0.0) {
                stringResource(
                    R.string.total_user_result,
                    testResult.score,
                    testResult.maxScore
                )
            } else {
                stringResource(
                    R.string.total_user_result_int,
                    testResult.score.toInt(),
                    testResult.maxScore
                )
            }
            Text(
                text = score,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(30.dp)
            )
        }
    }
}