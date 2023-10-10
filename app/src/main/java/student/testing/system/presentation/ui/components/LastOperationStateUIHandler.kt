package student.testing.system.presentation.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import student.testing.system.annotations.NotScreenState
import student.testing.system.domain.operationTypes.OperationType
import student.testing.system.domain.states.OperationState
import student.testing.system.presentation.ui.stateWrapper.StateWrapper

/**
 * Used for temporary and short-lived states caused by the last operation
 * @param onLoading if you want override default loading
 */
@OptIn(NotScreenState::class)
@Composable
fun <T> LastOperationStateUIHandler(
    stateWrapper: StateWrapper<OperationState<T>>,
    snackbarHostState: SnackbarHostState,
    onLoading: ((OperationType) -> Unit)? = null,
    onError: ((String, Int, OperationType) -> Unit)? = null
) {
    with(stateWrapper.uiState) {
        when (this) {
            is OperationState.Loading -> onLoading?.invoke(operationType) ?: LoadingIndicator()
            is OperationState.Error -> {
                LaunchedEffect(Unit) { // the key define when the block is relaunched
                    onError?.invoke(exception, code, operationType)
                        ?: snackbarHostState.showSnackbar(
                            exception
                        ).apply { stateWrapper.onReceive() }
                }
            }

            is OperationState.Empty, // it mustn't reach here, it must be replaced with Success in the ViewModel
            is OperationState.Success,// for this use method below
            is OperationState.NoState -> {
                // do nothing
            }
        }
    }
}

// TODO написать что succes надо бы по-хорошему ловить в VM
//  и потом уже кидать через свои FunctionallityState сделать
@OptIn(NotScreenState::class)
@Composable
fun <T> LastOperationStateUIHandler(
    stateWrapper: StateWrapper<OperationState<T>>,
    snackbarHostState: SnackbarHostState,
    onLoading: ((OperationType) -> Unit)? = null,
    onError: ((OperationType) -> Unit)? = null,
    onSuccess: @Composable (T, OperationType) -> Pair<AllOperationTypesWasHandled, AllCastsWasChecked>,
) {
    with(stateWrapper.uiState) {
        when (this) {
            is OperationState.Loading -> onLoading?.invoke(operationType) ?: LoadingIndicator()

            is OperationState.Success -> {
                onSuccess.invoke(data, operationType)
                stateWrapper.onReceive()
            }

            is OperationState.Error -> {
                LaunchedEffect(Unit) { // the key define when the block is relaunched
                    onError?.invoke(operationType) ?: snackbarHostState.showSnackbar(exception)
                    stateWrapper.onReceive()
                }
            }

            is OperationState.Empty, // it mustn't reach here, it must be replaced with Success in the ViewModel
            is OperationState.NoState -> {
                // do nothing
            }
        }
    }
}

object AllOperationTypesWasHandled
object AllCastsWasChecked