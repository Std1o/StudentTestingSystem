package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import student.testing.system.annotations.NotScreenState
import student.testing.system.domain.states.DataState

/**
 * BaseViewModel is an abstract class that provides a base implementation for ViewModels in the app.
 * It contains a StateFlow that represents the current state of the UI, and a method for launching
 * requests and updating the UI state based on the response.
 *
 * @param T The type of data that the ViewModel will handle.
 */
open class BaseViewModel<T> : ViewModel(), ResettableViewModel {

    private val _uiState = MutableStateFlow<DataState<T>>(DataState.NoState)
    val uiState: StateFlow<DataState<T>> = _uiState.asStateFlow()

    /**
     * Launches a request and updates the UI state based on the response.
     *
     * @param requestResult A Flow representing the result of the request
     * @param optionalCallback An optional callback function that will be called with each DataState emitted by the request result.
     */
    @OptIn(NotScreenState::class)
    protected fun launchRequest(
        requestResult: DataState<T>,
        onSuccess: (T) -> Unit = {},
    ) {
        _uiState.value = DataState.Loading
        if (requestResult is DataState.Success) onSuccess.invoke(requestResult.data)
        _uiState.value = requestResult
    }

    /**
     * Use if you sure that request when success returns 204
     */
    @OptIn(NotScreenState::class)
    protected fun <E> launchRequest(
        requestResult: DataState<Void>,
        successData: E,
    ): DataState<E> {
        return if (requestResult is DataState.Empty) {
            DataState.Success(successData)
        } else {
            DataState.Error("")
        }
    }

    override fun resetState() {
        _uiState.value = DataState.NoState
    }
}