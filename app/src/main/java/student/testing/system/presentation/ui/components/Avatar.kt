package student.testing.system.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun Avatar(name: String) {
    Text(
        modifier = Modifier
            .padding(16.dp)
            .size(22.dp)
            .drawBehind {
                drawCircle(
                    color = Color(
                        Random.nextFloat(),
                        Random.nextFloat(),
                        Random.nextFloat(),
                        1F
                    ),
                    radius = this.size.maxDimension,
                )
            },
        text = name.first().toString().uppercase(),
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = 16.sp
    )
}