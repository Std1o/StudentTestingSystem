@file:OptIn(ExperimentalMaterial3Api::class)

package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.domain.auth.AuthState
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.EmailTextField
import student.testing.system.presentation.ui.components.PasswordTextField
import student.testing.system.presentation.viewmodels.SignUpViewModel

@Composable
fun SignUpScreen() {
    val viewModel = hiltViewModel<SignUpViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    CenteredColumn {
        Text(text = "fjfjfjjdsdjffjjksksk")
        val isEmailError = uiState is AuthState.EmailError
        val isPasswordError = uiState is AuthState.PasswordError
        val email = EmailTextField(
            viewModel = viewModel,
            isEmailError = isEmailError,
            errorText = if (isEmailError) (uiState as AuthState.EmailError).messageResId else 0
        )
        val password = PasswordTextField(
            viewModel = viewModel,
            isPasswordError = isPasswordError,
            errorText = if (isPasswordError) (uiState as AuthState.PasswordError).messageResId else 0
        )
        Button(
            onClick = {
                scope.launch {
                    viewModel.signUp(email = email, username = "test", password = password)
                }
            }, modifier = Modifier
                .padding(top = 30.dp)
                .height(45.dp)
                .width(250.dp)
        ) { Text(stringResource(R.string.sign_in)) }
    }
}

@Composable
@Preview(showSystemUi = true)
fun SignUpScreenPreview() {
    SignUpScreenPreview()
}