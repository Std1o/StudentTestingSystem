package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import student.testing.system.annotations.FunctionalityState
import student.testing.system.annotations.NotScreenState
import student.testing.system.data.mapper.ToOperationStateMapper
import student.testing.system.domain.states.OperationState
import student.testing.system.domain.states.RequestState

/**
 * OperationViewModel contains a StateFlow that broadcasts last operation state,
 * and a method that launching operations and updating last operation state based on the response.
 *
 * @param State State that is used for this functionality
 * @param T Type of data that comes from the server when performing the operation
 */

open class OperationViewModel<@FunctionalityState State, T> : ViewModel(), ResettableViewModel {

    private val _lastOperationState = MutableStateFlow<OperationState<T>>(RequestState.NoState)
    val lastOperationState: StateFlow<OperationState<T>> = _lastOperationState.asStateFlow()

    private val toOperationStateMapper =
        ToOperationStateMapper<State, T>()

    /**
     * Launches operations and updating last operation state based on the response.
     *
     * @param call Suspend fun that will be called here
     * @param onSuccess An optional callback function that may be called for some ViewModel businesses
     */
    @OptIn(NotScreenState::class)
    protected suspend fun executeOperation(
        call: suspend () -> State,
        onSuccess: (T) -> Unit = {},
    ): State {
        var requestResult: State
        val request = viewModelScope.async {
            _lastOperationState.value = RequestState.Loading
            requestResult = call()
            val operationState = toOperationStateMapper.map(requestResult)
            if (operationState is RequestState.Success) onSuccess.invoke(operationState.data)
            _lastOperationState.value = operationState
            requestResult
        }
        return request.await()
    }

    override fun resetState() {
        _lastOperationState.value = RequestState.NoState
    }
}