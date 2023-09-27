package student.testing.system.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import student.testing.system.R
import student.testing.system.presentation.ui.screens.login.PasswordContentState
import student.testing.system.presentation.ui.stateWrappers.OnReceiveListener
import student.testing.system.presentation.ui.stateWrappers.UIStateWrapper
import student.testing.system.presentation.viewmodels.ResettableViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    onReceiveListener: OnReceiveListener,
    contentState: PasswordContentState,
    isPasswordError: Boolean,
    @StringRes errorText: Int
): String {
    var isPasswordError = isPasswordError
    var password by remember { mutableStateOf(TextFieldValue(contentState.password)) }
    var passwordVisible by remember { mutableStateOf(contentState.isVisible) }
    OutlinedTextField(
        value = password,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        label = { Text(stringResource(R.string.password)) },
        isError = isPasswordError,
        supportingText = {
            if (isPasswordError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(errorText),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        onValueChange = {
            password = it
            contentState.password = it.text
            isPasswordError = false
            onReceiveListener.onReceive()
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            if (isPasswordError) {
                Icon(
                    Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error
                )
                return@OutlinedTextField
            }
            val image = if (passwordVisible) Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            // Localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"

            // Toggle button to hide or display password
            IconButton(onClick = {
                passwordVisible = !passwordVisible
                contentState.isVisible = passwordVisible
            }) {
                Icon(imageVector = image, description)
            }
        })
    return password.text
}