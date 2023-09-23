package student.testing.system.presentation.ui.screens

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import student.testing.system.presentation.ui.components.CenteredColumn

@Composable
fun TestsScreen() {
    Surface {
        CenteredColumn {
            Text(text = "Тесты")
        }
    }
}