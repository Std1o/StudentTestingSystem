package student.testing.system.presentation.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import student.testing.system.annotations.NotScreenState
import student.testing.system.domain.states.DataState
import student.testing.system.presentation.viewmodels.ResettableViewModel

@OptIn(NotScreenState::class)
@Composable
fun <T> SimpleUIStateHandler(
    uiState: DataState<T>,
    snackbarHostState: SnackbarHostState,
    viewModel: ResettableViewModel,
    onSuccess: @Composable (T) -> Unit,
) {
    when (uiState) {
        is DataState.Loading -> LoadingIndicator()

        is DataState.Success -> {
            onSuccess.invoke(uiState.data)
            viewModel.resetState()
        }

        is DataState.Error -> {
            LaunchedEffect(Unit) { // the key define when the block is relaunched
                snackbarHostState.showSnackbar(uiState.exception)
                viewModel.resetState()
            }
        }

        is DataState.Empty, // it mustn't reach here, it must be replaced with Success in the ViewModel
        is DataState.NoState,
        is DataState.ValidationError -> { // will be moved to individual state
            // do nothing
        }
    }
}