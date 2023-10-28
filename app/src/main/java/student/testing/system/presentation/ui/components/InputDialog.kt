package student.testing.system.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import student.testing.system.R
import stdio.godofappstates.core.presentation.stateWrapper.OnReceiveListener

@Composable
fun InputDialog(
    @StringRes titleResId: Int,
    @StringRes hintResId: Int,
    @StringRes positiveButtonResId: Int = R.string.ok,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    isError: Boolean = false,
    @StringRes errorText: Int = 0,
    onTextChanged: (() -> Unit)? = null,
    onDismiss: () -> Unit,
    onPositiveClick: (String) -> Unit
) {
    var isError = isError
    var inputtedText by rememberSaveable { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            //shape = MaterialTheme.shapes.medium,
            shape = RoundedCornerShape(10.dp),
            // modifier = modifier.size(280.dp, 240.dp)
            modifier = Modifier.padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
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
                        onValueChange = {
                            inputtedText = it
                            isError = false
                            onTextChanged?.invoke()
                        },
                        modifier = Modifier.padding(8.dp),
                        label = { Text(stringResource(id = hintResId)) },
                        keyboardOptions = KeyboardOptions(
                            capitalization = capitalization,
                            imeAction = ImeAction.Done
                        ),
                        isError = isError,
                        supportingText = {
                            if (isError) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(errorText),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        trailingIcon = {
                            if (isError) Icon(
                                Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )

                    Row {
                        OutlinedButton(
                            onClick = {
                                onDismiss()
                            },
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
}