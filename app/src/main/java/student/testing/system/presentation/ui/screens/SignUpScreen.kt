@file:OptIn(ExperimentalMaterial3Api::class)

package student.testing.system.presentation.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.data.mapper.ToOperationStateMapper
import student.testing.system.domain.states.AuthState
import student.testing.system.domain.states.SignUpState
import student.testing.system.models.PrivateUser
import student.testing.system.presentation.ui.activity.MainActivityNew
import student.testing.system.presentation.ui.components.LastOperationStateUIHandler
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.EmailTextField
import student.testing.system.presentation.ui.components.PasswordTextField
import student.testing.system.presentation.viewmodels.ResettableViewModel
import student.testing.system.presentation.viewmodels.SignUpViewModel

@Composable
fun SignUpScreen() {
    val viewModel = hiltViewModel<SignUpViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val lastOperationState by viewModel.lastOperationState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val contentState = viewModel.contentState
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->
        CenteredColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            val isEmailError = uiState is AuthState.EmailError
            val isPasswordError = uiState is AuthState.PasswordError
            val isUsernameError = uiState is SignUpState.NameError
            val username = UsernameTextField(
                viewModel = viewModel,
                isUsernameError = isUsernameError,
                errorText = if (isUsernameError) (uiState as SignUpState.NameError).messageResId else 0
            )
            val email = EmailTextField(
                viewModel = viewModel,
                contentState = contentState.emailContentState,
                isEmailError = isEmailError,
                errorText = if (isEmailError) (uiState as AuthState.EmailError).messageResId else 0
            )
            val password = PasswordTextField(
                viewModel = viewModel,
                contentState = contentState.passwordContentState,
                isPasswordError = isPasswordError,
                errorText = if (isPasswordError) (uiState as AuthState.PasswordError).messageResId else 0
            )
            Button(
                onClick = {
                    scope.launch {
                        viewModel.signUp(email = email, username = username, password = password)
                    }
                }, modifier = Modifier
                    .padding(top = 30.dp)
                    .height(45.dp)
                    .width(250.dp)
            ) { Text(stringResource(R.string.sign_up)) }
        }
    }
    LastOperationStateUIHandler(lastOperationState, snackbarHostState, viewModel) {
        val activity = (LocalContext.current as? Activity)
        activity?.finish()
        activity?.startActivity(Intent(activity, MainActivityNew::class.java))
    }
}

@Composable
fun UsernameTextField(
    viewModel: ResettableViewModel,
    isUsernameError: Boolean,
    @StringRes errorText: Int
): String {
    var isUsernameError = isUsernameError
    var username by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(value = username,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        label = { Text(stringResource(R.string.name)) },
        isError = isUsernameError,
        supportingText = {
            if (isUsernameError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(errorText),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        onValueChange = {
            username = it
            isUsernameError = false
            viewModel.resetState()
        },
        trailingIcon = {
            if (isUsernameError) Icon(
                Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error
            )
        })
    return username.text
}