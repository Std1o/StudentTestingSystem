package student.testing.system.presentation.ui.components

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
import student.testing.system.domain.login.LoginState
import student.testing.system.presentation.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> PasswordTextField(uiState: LoginState<T>, viewModel: LoginViewModel): String {
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    OutlinedTextField(value = password,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        label = { Text(stringResource(R.string.password)) },
        isError = isPasswordError,
        supportingText = {
            if (isPasswordError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource((uiState as LoginState.PasswordError).messageResId),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        onValueChange = {
            password = it
            isPasswordError = false
            viewModel.resetState()
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
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        })
    if (uiState is LoginState.PasswordError) {
        isPasswordError = true
    }
    return password.text
}