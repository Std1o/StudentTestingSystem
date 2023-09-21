@file:OptIn(ExperimentalMaterial3Api::class)

package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.domain.login.LoginState
import student.testing.system.presentation.ui.activity.ui.theme.LoginTextColor
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.LoadingIndicator
import student.testing.system.presentation.viewmodels.LoginViewModel

@Composable
fun LoginScreen() {
    val viewModel: LoginViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState(LoginState.Initial)
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var passwordVisible by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Surface {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
        ) { contentPadding ->
            CenteredColumn(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                Text(
                    text = stringResource(R.string.app_name),
                    color = LoginTextColor,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 100.dp)
                )
                OutlinedTextField(value = email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    label = { Text(stringResource(R.string.e_mail)) },
                    isError = isEmailError,
                    supportingText = {
                        if (isEmailError) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource((uiState as LoginState.EmailError).messageResId),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    onValueChange = {
                        email = it
                        isEmailError = false
                        viewModel.resetState()
                    },
                    trailingIcon = {
                        if (isEmailError)
                            Icon(
                                Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colorScheme.error
                            )
                    })
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
                                Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colorScheme.error
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
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.auth(email = email.text, password = password.text)
                        }
                    }, modifier = Modifier
                        .padding(top = 30.dp)
                        .height(45.dp)
                        .width(250.dp)
                ) {
                    Text(stringResource(R.string.sign_in))
                }
                Text(
                    text = stringResource(R.string.registration),
                    color = LoginTextColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
        println(uiState)
        if (uiState is LoginState.Loading) {
            LoadingIndicator()
        } else if (uiState is LoginState.EmailError) {
            isEmailError = true
        } else if (uiState is LoginState.PasswordError) {
            isPasswordError = true
        } else if (uiState is LoginState.Error) {
            LaunchedEffect(Unit) { // the key define when the block is relaunched
                scope.launch {
                    snackbarHostState.showSnackbar((uiState as LoginState.Error).exception)
                }
            }

        }
    }
}