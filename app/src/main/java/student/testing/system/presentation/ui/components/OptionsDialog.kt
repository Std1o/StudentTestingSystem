package student.testing.system.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun OptionsDialog(options: List<String>, onDismiss: () -> Unit, onOptionSelected: (Int) -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(modifier = Modifier.clip(RoundedCornerShape(10.dp))) {
            LazyColumn {
                itemsIndexed(options) { index, option ->
                    Text(
                        text = option,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(index)
                                onDismiss()
                            }
                            .padding(vertical = 16.dp, horizontal = 16.dp)
                    )
                }
            }
        }
    }
}