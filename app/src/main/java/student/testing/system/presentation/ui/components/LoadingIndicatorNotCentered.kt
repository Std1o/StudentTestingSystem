package student.testing.system.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import student.testing.system.presentation.ui.activity.ui.theme.Teal200

@Composable
fun LoadingIndicatorNotCentered(
    modifier: Modifier = Modifier,
    color: Color = Teal200,
) {
    Box(contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = modifier,
                color = color,
            )
        }
    }
}