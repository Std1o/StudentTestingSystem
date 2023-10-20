package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import student.testing.system.domain.states.OperationState

/**
 * BaseViewModel is an abstract class that provides a base implementation for ViewModels in the app.
 * It contains a StateFlow that represents the current state of the UI, and a method for launching
 * requests and updating the UI state based on the response.
 *
 * @param T The type of data that the ViewModel will handle.
 */
// TODO remove
open class BaseViewModel<T> : ViewModel() {

    private val _uiState = MutableStateFlow<OperationState<T>>(OperationState.NoState)
    val uiState: StateFlow<OperationState<T>> = _uiState.asStateFlow()

    /**
     * Launches a request and updates the UI state based on the response.
     *
     * @param requestResult A Flow representing the result of the request
     * @param onSuccess An optional callback function that will be called with each DataState emitted by the request result.
     */
    protected fun launchRequest(
        requestResult: OperationState<T>,
        onSuccess: (T) -> Unit = {},
    ) {
        _uiState.value = OperationState.Loading()
        if (requestResult is OperationState.Success) onSuccess.invoke(requestResult.data)
        _uiState.value = requestResult
    }
}