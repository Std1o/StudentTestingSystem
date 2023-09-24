package student.testing.system.presentation.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import student.testing.system.domain.states.DataState

@Composable
fun <T> SimpleUIStateHandler(
    uiState: DataState<T>,
    snackbarHostState: SnackbarHostState,
    onSuccess: @Composable (T) -> Unit
) {
    if (uiState is DataState.Loading) {
        LoadingIndicator()
    } else if (uiState is DataState.Success) {
        onSuccess.invoke(uiState.data)
    } else if (uiState is DataState.Error) {
        LaunchedEffect(Unit) { // the key define when the block is relaunched
            snackbarHostState.showSnackbar(uiState.exception)
        }
    }
}