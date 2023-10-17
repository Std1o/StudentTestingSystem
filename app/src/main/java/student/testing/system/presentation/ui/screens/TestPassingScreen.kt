package student.testing.system.presentation.ui.screens

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import student.testing.system.presentation.ui.components.CenteredColumn

@Composable
fun TestPassingScreen() {
    Surface {
        CenteredColumn {
            Text(text = "Прохождение теста")
        }
    }
}