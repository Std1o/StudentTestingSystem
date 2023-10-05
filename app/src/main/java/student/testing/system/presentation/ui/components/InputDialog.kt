package student.testing.system.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import student.testing.system.R

@Composable
fun InputDialog(
    @StringRes titleResId: Int,
    @StringRes hintResId: Int,
    @StringRes positiveButtonResId: Int = R.string.ok,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    onDismiss: () -> Unit,
    onPositiveClick: (String) -> Unit
) {
    val context = LocalContext.current
    var inputtedText by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            //shape = MaterialTheme.shapes.medium,
            shape = RoundedCornerShape(10.dp),
            // modifier = modifier.size(280.dp, 240.dp)
            modifier = Modifier.padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                Modifier
                    .background(Color.White)
            ) {

                Text(
                    text = stringResource(id = titleResId),
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )

                OutlinedTextField(
                    value = inputtedText,
                    onValueChange = { inputtedText = it }, modifier = Modifier.padding(8.dp),
                    label = { Text(stringResource(id = hintResId)) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = capitalization,
                        imeAction = ImeAction.Done
                    ),
                )

                Row {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }


                    Button(
                        onClick = {
                            onPositiveClick(inputtedText)
                            onDismiss()
                        },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = stringResource(id = positiveButtonResId))
                    }
                }


            }
        }
    }
}