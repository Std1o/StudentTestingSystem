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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import student.testing.system.presentation.ui.models.RequiredFieldContentState
import student.testing.system.presentation.ui.stateWrapper.OnReceiveListener

@Composable
fun requiredTextField(
    onReceiveListener: OnReceiveListener,
    contentState: RequiredFieldContentState,
    isError: Boolean,
    @StringRes errorText: Int,
    @StringRes hint: Int
): String {
    var localIsError = isError
    var fieldValue by remember { mutableStateOf(TextFieldValue(contentState.fieldValue)) }
    OutlinedTextField(value = fieldValue,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
            contentState.fieldValue = it.text
            localIsError = false
            onReceiveListener.onReceive()
        },
        trailingIcon = {
            if (localIsError) Icon(
                Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error
            )
        })
    return fieldValue.text
}