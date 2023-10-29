package student.testing.system.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MediumButton(@StringRes text: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(bottom = 20.dp)
            .height(45.dp)
            .width(200.dp)
    ) { Text(stringResource(text)) }
}