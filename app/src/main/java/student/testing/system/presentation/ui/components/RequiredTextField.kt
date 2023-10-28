package student.testing.system.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import student.testing.system.presentation.ui.models.RequiredFieldState
import stdio.godofappstates.core.presentation.stateWrapper.OnReceiveListener

@Composable
fun requiredTextField(
    modifier: Modifier = Modifier,
    onTextChanged: () -> Unit,
    fieldState: RequiredFieldState,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    isError: Boolean,
    @StringRes errorText: Int,
    @StringRes hint: Int
): String {
    var localIsError = isError
    var fieldValue by remember { mutableStateOf(TextFieldValue(fieldState.fieldValue)) }
    OutlinedTextField(
        modifier = modifier,
        value = fieldValue,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = capitalization
        ),
        label = { Text(stringResource(hint)) },
        isError = localIsError,
        supportingText = {
            if (localIsError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(errorText),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        onValueChange = {
            fieldValue = it
            fieldState.fieldValue = it.text
            localIsError = false
            onTextChanged()
        },
        trailingIcon = {
            if (localIsError) Icon(
                Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error
            )
        })
    return fieldValue.text
}