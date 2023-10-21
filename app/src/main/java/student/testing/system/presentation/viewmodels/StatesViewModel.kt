package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.godofappstates.util.GenericsAutoCastIsWrong
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import stdio.godofappstates.annotations.FunctionalityState
import stdio.godofappstates.annotations.StillLoading
import student.testing.system.data.mapper.ToOperationStateMapper
import com.stdio.godofappstates.domain.OperationType
import student.testing.system.domain.states.LoadableData
import student.testing.system.domain.states.OperationState
import student.testing.system.presentation.ui.stateWrapper.StateWrapper
import java.util.LinkedList
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.reflect


/**
 * OperationViewModel contains a StateFlow that broadcasts last operation state,
 * and a method that launching operations and updating last operation state based on the response.
 *
 * @param State State that is used for this functionality
 * @param T Type of data that comes from the server when performing the operation
 */

open class StatesViewModel : ViewModel() {

    private val _lastOperationStateWrapper = StateWrapper.mutableStateFlow<OperationState<Any>>()
    val lastOperationStateWrapper = _lastOperationStateWrapper.asStateFlow()

    private val requestsQueue = LinkedList<String>()


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


    // executeOperation()
    // __________________________________________________________________________________
    /**
     * Launches operations and updating last operation state based on the response.
     *
     * Your coroutine must contain at least one val/var or you must specify explicit generics for this method!
     *  [Watch issue](https://github.com/Kotlin/kotlinx.coroutines/issues/3904)
     *
     * @param call Suspend fun that will be called here
     * @param onSuccess An optional callback function that may be called for some ViewModel businesses
     * @param type for auto cast. Believe me, you don't want to write that long generic
     *
     * Example:
     *
     * ```
     * PrivateUser::class
     * ```
     * @param operationType for loading
     */
    protected suspend inline fun <reified FlowOrState, T : Any> executeOperation(
        noinline call: suspend () -> FlowOrState,
        type: KClass<T>,
        operationType: OperationType = OperationType.DefaultOperation,
        noinline onEmpty204: () -> Unit = {},
        noinline onSuccess: (T) -> Unit = {},
    ): FlowOrState {
        return if (FlowOrState::class.starProjectedType.classifier == Flow::class) {
            flowExecuteOperation(
                call as suspend () -> Flow<*>,
                type,
                operationType,
                onEmpty204,
                onSuccess
            ) as FlowOrState
        } else {
            stateExecuteOperation(
                call,
                type,
                operationType,
                onEmpty204,
                onSuccess
            )
        }
    }

    @PublishedApi
    internal suspend fun <@FunctionalityState State, T : Any> stateExecuteOperation(
        call: suspend () -> State,
        type: KClass<T>,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty204: () -> Unit = {},
        onSuccess: (T) -> Unit = {},
    ): State {
        _lastOperationStateWrapper.value = StateWrapper(OperationState.Loading(operationType))
        if (_lastOperationStateWrapper.value.uiState is OperationState.Loading) {
            requestsQueue.offer(type.toString())
        }
        var requestResult: State
        val request = viewModelScope.async {
            // Call launching
            requestResult = call()
            if (requestResult is Unit) throw GenericsAutoCastIsWrong()

            // Working with OperationState
            val operationState = ToOperationStateMapper<State, T>().map(requestResult)
            if (operationState is OperationState.Success<T>) onSuccess.invoke(operationState.data)
            if (operationState is OperationState.Empty204) onEmpty204.invoke()
            _lastOperationStateWrapper.value = StateWrapper(operationState)

            requestResult
        }
        return getAwaitedResult(request)
    }

    /**
     * Если use case отправляет какие-то промежуточные результаты
     */
    @PublishedApi
    internal suspend fun <@FunctionalityState State, T : Any> flowExecuteOperation(
        call: suspend () -> Flow<State>,
        type: KClass<T>,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty204: () -> Unit = {},
        onSuccess: (T) -> Unit = {},
    ): StateFlow<State> {
        _lastOperationStateWrapper.value = StateWrapper(OperationState.Loading(operationType))
        val mutableStateFlow = call().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = OperationState.Loading(operationType) as State
        )
        if (_lastOperationStateWrapper.value.uiState is OperationState.Loading) {
            requestsQueue.offer(type.toString())
        }
        // иначе код выполняется синхронно
        // и флоу вернется только когда весь этот участок будет пройден
        viewModelScope.launch {
            mutableStateFlow.collect { requestResult ->
                if (requestResult is Unit) throw GenericsAutoCastIsWrong()
                val operationState = ToOperationStateMapper<State, Any>().map(requestResult)
                if (operationState is OperationState.Success) onSuccess.invoke(operationState.data as T)
                if (operationState is OperationState.Empty204) onEmpty204.invoke()
                _lastOperationStateWrapper.value = StateWrapper(operationState)
                pollFromQueueForFlow(requestResult)
                showLoadingForFlowIfNeed()
            }
        }
        return mutableStateFlow
    }


    // executeEmptyOperation()
    // __________________________________________________________________________________
    protected suspend inline fun <reified FlowOrState> executeEmptyOperation(
        noinline call: suspend () -> FlowOrState,
        operationType: OperationType = OperationType.DefaultOperation,
        noinline onEmpty204: () -> Unit = {},
    ): FlowOrState {
        return if (FlowOrState::class.starProjectedType.classifier == Flow::class) {
            flowExecuteEmptyOperation(
                call as suspend () -> Flow<*>,
                operationType,
                onEmpty204
            ) as FlowOrState
        } else {
            stateExecuteEmptyOperation(call, operationType, onEmpty204)
        }
    }

    @PublishedApi
    internal suspend fun <@FunctionalityState State> stateExecuteEmptyOperation(
        call: suspend () -> State,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty204: () -> Unit = {},
    ): State {
        return stateExecuteEmptyOrWithDataIgnoringOperation(
            call = call,
            operationType = operationType,
            onSuccess = {},
            onEmpty204 = { onEmpty204() })
    }

    @PublishedApi
    internal suspend fun <@FunctionalityState State> flowExecuteEmptyOperation(
        call: suspend () -> Flow<State>,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty204: () -> Unit = {},
    ): Flow<State> {
        return flowExecuteEmptyOrWithDataIgnoringOperation(
            call = call,
            operationType = operationType,
            onSuccess = {},
            onEmpty204 = { onEmpty204() })
    }


    // executeOperationAndIgnoreData()
    // __________________________________________________________________________________
    protected suspend inline fun <reified FlowOrState> executeOperationAndIgnoreData(
        noinline call: suspend () -> FlowOrState,
        operationType: OperationType = OperationType.DefaultOperation,
        noinline onSuccess: () -> Unit = {}
    ): FlowOrState {
        return if (FlowOrState::class.starProjectedType.classifier == Flow::class) {
            flowExecuteOperationAndIgnoreData(
                call as suspend () -> Flow<*>,
                operationType,
                onSuccess
            ) as FlowOrState
        } else {
            stateExecuteOperationAndIgnoreData(call, operationType, onSuccess)
        }
    }

    /**
     * Используется если тип данных в onSuccess не важен
     */
    @PublishedApi
    internal suspend fun <@FunctionalityState State> stateExecuteOperationAndIgnoreData(
        call: suspend () -> State,
        operationType: OperationType = OperationType.DefaultOperation,
        onSuccess: () -> Unit = {},
    ): State {
        return stateExecuteEmptyOrWithDataIgnoringOperation(
            call = call,
            operationType = operationType,
            onSuccess = { onSuccess() },
            onEmpty204 = {})
    }

    /**
     * Используется если тип данных в onSuccess не важен
     */
    @PublishedApi
    internal suspend fun <@FunctionalityState State> flowExecuteOperationAndIgnoreData(
        call: suspend () -> Flow<State>,
        operationType: OperationType = OperationType.DefaultOperation,
        onSuccess: () -> Unit = {},
    ): Flow<State> {
        return flowExecuteEmptyOrWithDataIgnoringOperation(
            call = call,
            operationType = operationType,
            onSuccess = { onSuccess() },
            onEmpty204 = {})
    }


    // Slaves
    // __________________________________________________________________________________
    private suspend fun <@FunctionalityState State> stateExecuteEmptyOrWithDataIgnoringOperation(
        call: suspend () -> State,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty204: () -> Unit,
        onSuccess: () -> Unit,
    ): State {
        _lastOperationStateWrapper.value = StateWrapper(OperationState.Loading(operationType))
        if (_lastOperationStateWrapper.value.uiState is OperationState.Loading) {
            requestsQueue.offer(call.reflect()?.returnType.toString())
        }
        var requestResult: State
        val request = viewModelScope.async {
            // Call launching
            requestResult = call()
            if (requestResult is Unit) throw GenericsAutoCastIsWrong()

            // Working with OperationState
            val operationState = ToOperationStateMapper<State, Any>().map(requestResult)
            if (operationState is OperationState.Success) onSuccess.invoke()
            if (operationState is OperationState.Empty204) onEmpty204.invoke()
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

    private suspend fun <@FunctionalityState State> flowExecuteEmptyOrWithDataIgnoringOperation(
        call: suspend () -> Flow<State>,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty204: () -> Unit = {},
        onSuccess: () -> Unit = {},
    ): StateFlow<State> {
        _lastOperationStateWrapper.value = StateWrapper(OperationState.Loading(operationType))
        val mutableStateFlow = call().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = OperationState.Loading(operationType) as State
        )
        if (_lastOperationStateWrapper.value.uiState is OperationState.Loading) {
            requestsQueue.offer(call.reflect()?.returnType.toString())
        }
        // иначе код выполняется синхронно
        // и флоу вернется только когда весь этот участок будет пройден
        viewModelScope.launch {
            mutableStateFlow.collect { requestResult ->
                if (requestResult is Unit) throw GenericsAutoCastIsWrong()
                val operationState = ToOperationStateMapper<State, Any>().map(requestResult)
                if (operationState is OperationState.Success) onSuccess.invoke()
                if (operationState is OperationState.Empty204) onEmpty204.invoke()
                _lastOperationStateWrapper.value = StateWrapper(operationState)
                pollFromQueueForFlow(requestResult)
                showLoadingForFlowIfNeed()
            }
        }
        return mutableStateFlow
    }

    // значит что конечный резульатат получен и можно очистить очередь
    private fun <State> pollFromQueueForFlow(requestResult: State) {
        if (requestResult!!::class.hasAnnotation<StillLoading>()) {
            return
        }
        requestsQueue.poll()
    }

    private fun showLoadingForFlowIfNeed() {
        if (requestsQueue.isNotEmpty()) {
            // TODO мб складывать в requestsQueue как раз таки operationType,
            //  но с другой стороны не у всех он указан
            _lastOperationStateWrapper.value = StateWrapper(OperationState.Loading())
        }
    }
}

fun <State> State.protect() {}