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
import student.testing.system.presentation.ui.stateWrapper.UIStateWrapper
import java.util.LinkedList
import kotlin.reflect.jvm.reflect


/**
 * OperationViewModel contains a StateFlow that broadcasts last operation state,
 * and a method that launching operations and updating last operation state based on the response.
 *
 * @param State State that is used for this functionality
 * @param T Type of data that comes from the server when performing the operation
 */

open class OperationViewModel<@FunctionalityState State, T> : ViewModel() {

    private val _lastOperationStateWrapper =
        MutableStateFlow<UIStateWrapper<OperationState<T>, T>>(UIStateWrapper())
    val lastOperationStateWrapper: StateFlow<UIStateWrapper<OperationState<T>, T>> =
        _lastOperationStateWrapper.asStateFlow()

    private val toOperationStateMapper =
        ToOperationStateMapper<State, T>()

    private val requestsQueue = LinkedList<String>()

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
        if (_lastOperationStateWrapper.value.uiState is RequestState.Loading) {
            // actually you can stuff anything to queue, nothing will break
            // but return type is used for simplified debugging

            // ideally, get the name of the function that is called in call,
            // but it is not yet clear how to do this
            requestsQueue.offer(call.reflect()?.returnType.toString())
        }
        var requestResult: State
        val request = viewModelScope.async {
            _lastOperationStateWrapper.value = UIStateWrapper(RequestState.Loading)
            requestResult = call()
            val operationState = toOperationStateMapper.map(requestResult)
            if (operationState is RequestState.Success) onSuccess.invoke(operationState.data)
            _lastOperationStateWrapper.value = UIStateWrapper(operationState)
            requestResult
        }
        return request.await().also {
            requestsQueue.poll()
            if (requestsQueue.isNotEmpty()) {
                _lastOperationStateWrapper.value = UIStateWrapper(RequestState.Loading)
            }
        }
    }
}