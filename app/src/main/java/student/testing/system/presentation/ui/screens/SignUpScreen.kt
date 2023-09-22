@file:OptIn(ExperimentalMaterial3Api::class)

package student.testing.system.presentation.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.viewmodels.SignUpViewModel

@Composable
fun SignUpScreen() {
    val viewModel = hiltViewModel<SignUpViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    CenteredColumn {
        Text(text = "fjfjfjjdsdjffjjksksk")
//        val isEmailError = uiState is LoginState.EmailError
//        val email = EmailTextField(
//            viewModel = viewModel,
//            isEmailError = isEmailError,
//            errorText = if (isEmailError) (uiState as LoginState.EmailError).messageResId else 0
//        )
        // val password = PasswordTextField(uiState = uiState, viewModel = viewModel)
    }
}

@Composable
@Preview(showSystemUi = true)
fun SignUpScreenPreview() {
    SignUpScreenPreview()
}