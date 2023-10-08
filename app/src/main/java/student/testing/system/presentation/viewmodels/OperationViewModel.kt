package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import student.testing.system.annotations.FunctionalityState
import student.testing.system.annotations.IntermediateState
import student.testing.system.annotations.NotScreenState
import student.testing.system.common.GenericsAutoCastIsWrong
import student.testing.system.data.mapper.ToOperationStateMapper
import student.testing.system.domain.operationTypes.OperationType
import student.testing.system.domain.states.OperationState
import student.testing.system.domain.states.RequestState
import student.testing.system.presentation.ui.stateWrapper.UIStateWrapper
import java.util.LinkedList
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.reflect


/**
 * OperationViewModel contains a StateFlow that broadcasts last operation state,
 * and a method that launching operations and updating last operation state based on the response.
 *
 * @param State State that is used for this functionality
 * @param T Type of data that comes from the server when performing the operation
 */

open class OperationViewModel<T> : ViewModel() {

    private val _lastOperationStateWrapper =
        MutableStateFlow<UIStateWrapper<OperationState<T>>>(UIStateWrapper())
    val lastOperationStateWrapper: StateFlow<UIStateWrapper<OperationState<T>>> =
        _lastOperationStateWrapper.asStateFlow()

    private val toOperationStateMapper =
        ToOperationStateMapper<T>()

    private val requestsQueue = LinkedList<String>()

    /**
     * Launches operations and updating last operation state based on the response.
     *
     * Your coroutine must contain at least one val/var or you must specify explicit generics for this method!
     *  [Watch issue](https://github.com/Kotlin/kotlinx.coroutines/issues/3904)
     *
     * @param call Suspend fun that will be called here
     * @param onSuccess An optional callback function that may be called for some ViewModel businesses
     */
    @OptIn(NotScreenState::class)
    protected suspend fun <@FunctionalityState State> executeOperation(
        call: suspend () -> State,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty: () -> Unit = {},
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
            _lastOperationStateWrapper.value = UIStateWrapper(RequestState.Loading(operationType))
            println(operationType)
            requestResult = call()
            if (requestResult is Unit) throw GenericsAutoCastIsWrong()
            val operationState = toOperationStateMapper.map(requestResult as Any)
            if (operationState is RequestState.Success) onSuccess.invoke(operationState.data)
            if (operationState is RequestState.Empty) onEmpty.invoke()
            _lastOperationStateWrapper.value = UIStateWrapper(operationState)
            requestResult
        }
        return request.await().also {
            requestsQueue.poll()
            if (requestsQueue.isNotEmpty()) {
                // TODO мб складывать в requestsQueue как раз таки operationType,
                //  но с другой стороны не у всех он указан
                _lastOperationStateWrapper.value = UIStateWrapper(RequestState.Loading())
            }
        }
    }

    /**
     * Если use case отправляет какие-то промежуточные результаты
     */
    @OptIn(NotScreenState::class)
    protected fun <@FunctionalityState State> executeFlowOperation(
        requestFlow: Flow<State>,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty: () -> Unit = {},
        onSuccess: (T) -> Unit = {},
    ): Flow<State> {
        if (_lastOperationStateWrapper.value.uiState is RequestState.Loading) {
            // actually you can stuff anything to queue, nothing will break
            // but return type is used for simplified debugging

            // ideally, get the name of the function that is called in call,
            // but it is not yet clear how to do this
            requestsQueue.offer("todo: put normal object")
        }
        _lastOperationStateWrapper.value = UIStateWrapper(RequestState.Loading(operationType))
        // иначе код выполняется синхронно
        // и флоу вернется только когда весь этот участок будет пройден
        viewModelScope.launch {
            requestFlow.collect { requestResult ->
                if (requestResult is Unit) throw GenericsAutoCastIsWrong()
                val operationState = toOperationStateMapper.map(requestResult as Any)
                if (operationState is RequestState.Success) onSuccess.invoke(operationState.data)
                if (operationState is RequestState.Empty) onEmpty.invoke()
                _lastOperationStateWrapper.value = UIStateWrapper(operationState)
                // значит что конечный резульатат получен и можно очистить очередь
                if (operationState !is RequestState.Loading && operationState !is RequestState.NoState) {
                    requestsQueue.poll()
                }
                if (requestResult!!::class.hasAnnotation<IntermediateState>()) {
                    _lastOperationStateWrapper.value =
                        UIStateWrapper(RequestState.Loading(operationType))
                }

                if (requestsQueue.isNotEmpty()) {
                    // TODO мб складывать в requestsQueue как раз таки operationType,
                    //  но с другой стороны не у всех он указан
                    _lastOperationStateWrapper.value = UIStateWrapper(RequestState.Loading())
                }
            }
        }
        return requestFlow
    }
}

fun <State> State.protect() {}