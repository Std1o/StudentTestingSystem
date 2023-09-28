package student.testing.system.presentation.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import student.testing.system.annotations.NotScreenState
import student.testing.system.domain.operationTypes.OperationType
import student.testing.system.domain.states.OperationState
import student.testing.system.domain.states.RequestState
import student.testing.system.presentation.ui.stateWrapper.UIStateWrapper

/**
 * Used for temporary and short-lived states caused by the last operation
 */
@OptIn(NotScreenState::class)
@Composable
fun <T> LastOperationStateUIHandler(
    stateWrapper: UIStateWrapper<OperationState<T>, T>,
    snackbarHostState: SnackbarHostState,
    onSuccess: @Composable (T, OperationType) -> Unit,
) {
    with(stateWrapper.uiState) {
        when (this) {
            is RequestState.Loading -> LoadingIndicator()

            is RequestState.Success -> {
                onSuccess.invoke(data, operationType)
                stateWrapper.onReceive()
            }

            is RequestState.Error -> {
                LaunchedEffect(Unit) { // the key define when the block is relaunched
                    snackbarHostState.showSnackbar(exception)
                    stateWrapper.onReceive()
                }
            }

            is RequestState.Empty, // it mustn't reach here, it must be replaced with Success in the ViewModel
            is RequestState.NoState,
            is RequestState.ValidationError -> { // will be moved to individual state
                // do nothing
            }
        }
    }
}