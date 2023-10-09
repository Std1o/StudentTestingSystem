package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import student.testing.system.annotations.FunctionalityState
import student.testing.system.annotations.IntermediateState
import student.testing.system.annotations.NotScreenState
import student.testing.system.common.GenericsAutoCastIsWrong
import student.testing.system.data.mapper.ToOperationStateMapper
import student.testing.system.domain.operationTypes.OperationType
import student.testing.system.domain.states.LoadableData
import student.testing.system.domain.states.OperationState
import student.testing.system.presentation.ui.stateWrapper.StateWrapper
import java.util.LinkedList
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation


/**
 * OperationViewModel contains a StateFlow that broadcasts last operation state,
 * and a method that launching operations and updating last operation state based on the response.
 *
 * @param State State that is used for this functionality
 * @param T Type of data that comes from the server when performing the operation
 */

open class StatesViewModel : ViewModel() {

    private val _lastOperationStateWrapper =
        MutableStateFlow<StateWrapper<OperationState<Any>>>(StateWrapper())
    val lastOperationStateWrapper: StateFlow<StateWrapper<OperationState<Any>>> =
        _lastOperationStateWrapper.asStateFlow()

    private val requestsQueue = LinkedList<String>()

    protected suspend inline fun <reified FlowOrState, T : Any> executeOperation(
        noinline call: suspend () -> FlowOrState,
        type: KClass<T>,
        operationType: OperationType = OperationType.DefaultOperation,
        noinline onEmpty: () -> Unit = {},
        noinline onSuccess: (T) -> Unit = {},
    ): FlowOrState {
        return if (FlowOrState::class.java.isInstance(flow<T> { })) {
            executeOperationFlow(
                call as suspend () -> Flow<*>,
                type,
                operationType,
                onEmpty,
                onSuccess
            ) as FlowOrState
        } else {
            executeOperationState(
                call,
                type,
                operationType,
                onEmpty,
                onSuccess
            )
        }
    }

    /**
     * Launches operations and updating last operation state based on the response.
     *
     * Your coroutine must contain at least one val/var or you must specify explicit generics for this method!
     *  [Watch issue](https://github.com/Kotlin/kotlinx.coroutines/issues/3904)
     *
     * @param call Suspend fun that will be called here
     * @param onSuccess An optional callback function that may be called for some ViewModel businesses
     * @param type for auto cast. Believe me, you don't want to write that long generic
     * <b/>
     *
     * Example:
     * <b/>
     *
     * ```
     * PrivateUser::class
     * ```
     * @param operationType for loading
     */
    @OptIn(NotScreenState::class)
    @PublishedApi
    internal suspend fun <@FunctionalityState State, T : Any> executeOperationState(
        call: suspend () -> State,
        type: KClass<T>,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty: () -> Unit = {},
        onSuccess: (T) -> Unit = {},
    ): State {
        if (_lastOperationStateWrapper.value.uiState is OperationState.Loading) {
            requestsQueue.offer(type.toString())
        }
        var requestResult: State
        val request = viewModelScope.async {
            // Call launching
            _lastOperationStateWrapper.value = StateWrapper(OperationState.Loading(operationType))
            requestResult = call()
            if (requestResult is Unit) throw GenericsAutoCastIsWrong()

            // Working with OperationState
            val operationState = ToOperationStateMapper<State, T>().map(requestResult)
            if (operationState is OperationState.Success<T>) onSuccess.invoke(operationState.data)
            _lastOperationStateWrapper.value = StateWrapper(operationState)

            requestResult
        }
        return getAwaitedResult(request)
    }

    protected suspend fun <@FunctionalityState State> executeEmptyOperation(
        call: suspend () -> State,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty: () -> Unit = {},
    ): State {
        return executeEmptyOrWithDataIgnoringOperation(
            call = call,
            operationType = operationType,
            onSuccess = {},
            onEmpty = { onEmpty() })
    }

    /**
     * Используется если тип данных в onSuccess не важен
     */
    protected suspend fun <@FunctionalityState State> executeOperationAndIgnoreData(
        call: suspend () -> State,
        operationType: OperationType = OperationType.DefaultOperation,
        onSuccess: () -> Unit = {},
    ): State {
        return executeEmptyOrWithDataIgnoringOperation(
            call = call,
            operationType = operationType,
            onSuccess = { onSuccess() },
            onEmpty = {})
    }

    @OptIn(NotScreenState::class)
    private suspend fun <@FunctionalityState State> executeEmptyOrWithDataIgnoringOperation(
        call: suspend () -> State,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty: () -> Unit,
        onSuccess: () -> Unit,
    ): State {
        if (_lastOperationStateWrapper.value.uiState is OperationState.Loading) {
            requestsQueue.offer("element: ${requestsQueue.size + 1}")
        }
        var requestResult: State
        val request = viewModelScope.async {
            // Call launching
            _lastOperationStateWrapper.value = StateWrapper(OperationState.Loading(operationType))
            requestResult = call()
            if (requestResult is Unit) throw GenericsAutoCastIsWrong()

            // Working with OperationState
            val operationState = ToOperationStateMapper<State, Any>().map(requestResult)
            if (operationState is OperationState.Success<*>) onSuccess.invoke()
            if (operationState is OperationState.Empty) onEmpty.invoke()
            _lastOperationStateWrapper.value = StateWrapper(operationState)

            requestResult
        }
        return getAwaitedResult(request)
    }

    private suspend fun <State> getAwaitedResult(request: Deferred<State>) = request.await().also {
        requestsQueue.poll()
        if (requestsQueue.isNotEmpty()) {
            _lastOperationStateWrapper.value = StateWrapper(OperationState.Loading())
        }
    }

    /**
     * Если use case отправляет какие-то промежуточные результаты
     */
    @OptIn(NotScreenState::class)
    @PublishedApi
    internal suspend fun <@FunctionalityState State, T : Any> executeOperationFlow(
        call: suspend () -> Flow<State>,
        type: KClass<T>,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty: () -> Unit = {},
        onSuccess: (T) -> Unit = {},
    ): Flow<State> {
        val mutableSharedFlow = call().shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly
        )
        if (_lastOperationStateWrapper.value.uiState is OperationState.Loading) {
            // actually you can stuff anything to queue, nothing will break
            // but return type is used for simplified debugging

            // ideally, get the name of the function that is called in call,
            // but it is not yet clear how to do this
            requestsQueue.offer("todo: put normal object")
        }
        _lastOperationStateWrapper.value = StateWrapper(OperationState.Loading(operationType))
        // иначе код выполняется синхронно
        // и флоу вернется только когда весь этот участок будет пройден
        viewModelScope.launch {
            mutableSharedFlow.collect { requestResult ->
                if (requestResult is Unit) throw GenericsAutoCastIsWrong()
                val operationState = ToOperationStateMapper<State, Any>().map(requestResult)
                if (operationState is OperationState.Success) onSuccess.invoke(operationState.data as T)
                if (operationState is OperationState.Empty) onEmpty.invoke()
                _lastOperationStateWrapper.value = StateWrapper(operationState)
                // значит что конечный резульатат получен и можно очистить очередь
                if (operationState !is OperationState.Loading && operationState !is OperationState.NoState) {
                    requestsQueue.poll()
                }
                if (requestResult!!::class.hasAnnotation<IntermediateState>()) {
                    _lastOperationStateWrapper.value =
                        StateWrapper(OperationState.Loading(operationType))
                }

                if (requestsQueue.isNotEmpty()) {
                    // TODO мб складывать в requestsQueue как раз таки operationType,
                    //  но с другой стороны не у всех он указан
                    _lastOperationStateWrapper.value = StateWrapper(OperationState.Loading())
                }
            }
        }
        return mutableSharedFlow
    }

    // State - LoadableData
    suspend fun <State> loadData(
        call: suspend () -> State
    ): StateFlow<State> {
        val stateFlow = MutableStateFlow(LoadableData.Loading() as State)
        viewModelScope.launch {// for asynchrony
            var requestResult: State = call()
            stateFlow.emit(requestResult)
        }
        return stateFlow
    }
}

fun <State> State.protect() {}