package student.testing.system.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ExperimentalMaterial3Api
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
import student.testing.system.R
import student.testing.system.domain.states.OperationState
import student.testing.system.presentation.ui.screens.login.EmailContentState
import student.testing.system.presentation.ui.stateWrappers.UIStateWrapper
import student.testing.system.presentation.viewmodels.ResettableViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <State, T> EmailTextField(
    stateWrapper: UIStateWrapper<State, T>,
    contentState: EmailContentState,
    isEmailError: Boolean,
    @StringRes errorText: Int
): String {
    var isEmailError = isEmailError
    var email by remember { mutableStateOf(TextFieldValue(contentState.email)) }
    OutlinedTextField(
        value = email,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        label = { Text(stringResource(R.string.e_mail)) },
        isError = isEmailError,
        supportingText = {
            if (isEmailError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(errorText),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        onValueChange = {
            email = it
            contentState.email = it.text
            isEmailError = false
            stateWrapper.onReceive()
        },
        trailingIcon = {
            if (isEmailError) Icon(
                Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error
            )
        })
    return email.text
}