package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import student.testing.system.annotations.NotScreenState
import student.testing.system.domain.states.LoadableData
import student.testing.system.domain.states.OperationState

// TODO remove
open class BaseLoadableViewModel<T> : ViewModel(), ResettableViewModel {

    private val _uiState = MutableStateFlow<LoadableData<T>>(LoadableData.NoState)
    val uiState: StateFlow<LoadableData<T>> = _uiState.asStateFlow()

    /**
     * Launches a request and updates the UI state based on the response.
     *
     * @param requestResult A Flow representing the result of the request
     * @param onSuccess An optional callback function that will be called with each DataState emitted by the request result.
     */
    @OptIn(NotScreenState::class)
    protected fun launchRequest(
        requestResult: LoadableData<T>,
        onSuccess: (T) -> Unit = {},
    ) {
        _uiState.value = LoadableData.Loading()
        if (requestResult is LoadableData.Success) onSuccess.invoke(requestResult.data)
        _uiState.value = requestResult
    }

    override fun resetState() {
        _uiState.value = LoadableData.NoState
    }
}