package student.testing.system.presentation.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import stdio.lilith.core.domain.OperationType
import student.testing.system.domain.states.operationStates.OperationState

/**
 * Used for temporary and short-lived states caused by the last operation
 * @param onLoading if you want override default loading
 * @param onError if you want override on error default reaction
 */
@Composable
fun <T> UIReactionOnLastOperationState(
    operationState: OperationState<T>,
    event: OperationState<T>,
    snackbarHostState: SnackbarHostState,
    onLoading: ((OperationType) -> Unit)? = null,
    onError: ((String, Int, OperationType) -> Unit)? = null
) {
    with(operationState) {
        when (this) {
            is OperationState.Loading -> onLoading?.invoke(operationType) ?: LoadingIndicator()
            else -> {}
        }
    }
    with(event) {
        when (this) {
            is OperationState.ErrorSingle -> {
                LaunchedEffect(event) { // the key define when the block is relaunched
                    onError?.invoke(exception, code, operationType)
                        ?: snackbarHostState.showSnackbar(exception)
                }
            }

            else -> {}
        }
    }
}