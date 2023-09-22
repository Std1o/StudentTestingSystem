@file:OptIn(ExperimentalMaterial3Api::class)

package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.domain.auth.AuthState
import student.testing.system.presentation.ui.activity.ui.theme.LoginTextColor
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.EmailTextField
import student.testing.system.presentation.ui.components.LoadingIndicator
import student.testing.system.presentation.ui.components.PasswordTextField
import student.testing.system.presentation.viewmodels.LoginViewModel

@Composable
fun LoginScreen() {
    val viewModel = hiltViewModel<LoginViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
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
            Text(
                text = stringResource(R.string.app_name),
                color = LoginTextColor,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 100.dp)
            )
            val isEmailError = uiState is AuthState.EmailError
            val email = EmailTextField(
                viewModel = viewModel,
                isEmailError = isEmailError,
                errorText = if (isEmailError) (uiState as AuthState.EmailError).messageResId else 0
            )
            val password = PasswordTextField(uiState = uiState, viewModel = viewModel)
            Button(
                onClick = {
                    scope.launch { viewModel.auth(email = email, password = password) }
                }, modifier = Modifier
                    .padding(top = 30.dp)
                    .height(45.dp)
                    .width(250.dp)
            ) { Text(stringResource(R.string.sign_in)) }
            Text(
                text = stringResource(R.string.registration),
                color = LoginTextColor,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .clickable { viewModel.onNavigateToSignUp() }
            )
        }
    }
    if (uiState is AuthState.Loading) {
        LoadingIndicator()
    } else if (uiState is AuthState.Error) {
        LaunchedEffect(Unit) { // the key define when the block is relaunched
            scope.launch {
                snackbarHostState.showSnackbar((uiState as AuthState.Error).exception)
            }
        }

    }
}