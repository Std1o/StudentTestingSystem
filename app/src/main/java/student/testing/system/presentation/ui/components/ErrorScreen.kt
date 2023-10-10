package student.testing.system.presentation.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import student.testing.system.R

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    CenteredColumn {
        Text(
            text = stringResource(id = R.string.loading_error),
            textAlign = TextAlign.Center,
            color = Color.DarkGray
        )
        Text(
            text = message,
            textAlign = TextAlign.Center,
            color = Color(0xffff6685),
            modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)
        )
        OutlinedButton(
            onClick = {
                onRetry()
            }, modifier = Modifier
                .padding(top = 30.dp)
        ) { androidx.compose.material3.Text(stringResource(R.string.retry)) }
    }
}