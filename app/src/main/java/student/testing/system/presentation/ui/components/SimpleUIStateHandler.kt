package student.testing.system.presentation.ui.components

import android.app.Activity
import android.content.Intent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import student.testing.system.domain.auth.AuthState
import student.testing.system.presentation.ui.activity.MainActivityNew

@Composable
fun <T> SimpleUIStateHandler(
    uiState: AuthState<T>,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onSuccess: @Composable (T) -> Unit
) {
    if (uiState is AuthState.Loading) {
        LoadingIndicator()
    } else if (uiState is AuthState.Success) {
        onSuccess.invoke(uiState.data)
    } else if (uiState is AuthState.Error) {
        LaunchedEffect(Unit) { // the key define when the block is relaunched
            scope.launch {
                snackbarHostState.showSnackbar((uiState as AuthState.Error).exception)
            }
        }
    }
}