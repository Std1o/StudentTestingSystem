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
        return if (FlowOrState::class.starProjectedType.classifier == Flow::class) {
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
            if (operationState is OperationState.Empty) onEmpty.invoke()
            _lastOperationStateWrapper.value = StateWrapper(operationState)

            requestResult
        }
        return getAwaitedResult(request)
    }

    protected suspend inline fun <reified FlowOrState> executeEmptyOperation(
        noinline call: suspend () -> FlowOrState,
        operationType: OperationType = OperationType.DefaultOperation,
        noinline onEmpty: () -> Unit = {},
    ): FlowOrState {
        return if (FlowOrState::class.starProjectedType.classifier == Flow::class) {
            executeEmptyOperationFlow(
                call as suspend () -> Flow<*>,
                operationType,
                onEmpty
            ) as FlowOrState
        } else {
            executeEmptyOperationState(call, operationType, onEmpty)
        }
    }

    protected suspend inline fun <reified FlowOrState> executeOperationAndIgnoreData(
        noinline call: suspend () -> FlowOrState,
        operationType: OperationType = OperationType.DefaultOperation,
        noinline onSuccess: () -> Unit = {}
    ): FlowOrState {
        return if (FlowOrState::class.starProjectedType.classifier == Flow::class) {
            executeOperationAndIgnoreDataFlow(
                call as suspend () -> Flow<*>,
                operationType,
                onSuccess
            ) as FlowOrState
        } else {
            executeOperationAndIgnoreDataState(call, operationType, onSuccess)
        }
    }

    @PublishedApi
    internal suspend fun <@FunctionalityState State> executeEmptyOperationState(
        call: suspend () -> State,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty: () -> Unit = {},
    ): State {
        return executeEmptyOrWithDataIgnoringOperationState(
            call = call,
            operationType = operationType,
            onSuccess = {},
            onEmpty = { onEmpty() })
    }

    /**
     * Используется если тип данных в onSuccess не важен
     */
    @PublishedApi
    internal suspend fun <@FunctionalityState State> executeOperationAndIgnoreDataState(
        call: suspend () -> State,
        operationType: OperationType = OperationType.DefaultOperation,
        onSuccess: () -> Unit = {},
    ): State {
        return executeEmptyOrWithDataIgnoringOperationState(
            call = call,
            operationType = operationType,
            onSuccess = { onSuccess() },
            onEmpty = {})
    }

    @OptIn(NotScreenState::class)
    private suspend fun <@FunctionalityState State> executeEmptyOrWithDataIgnoringOperationState(
        call: suspend () -> State,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty: () -> Unit,
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
            requestsQueue.offer(type.toString())
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
                pollFromQueueForFlow(operationState)
                showLoadingForFlowIfNeed(requestResult, operationType)
            }
        }
        return mutableSharedFlow
    }

    @PublishedApi
    internal suspend fun <@FunctionalityState State> executeEmptyOperationFlow(
        call: suspend () -> Flow<State>,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty: () -> Unit = {},
    ): Flow<State> {
        return executeEmptyOrWithDataIgnoringOperationFlow(
            call = call,
            operationType = operationType,
            onSuccess = {},
            onEmpty = { onEmpty() })
    }

    /**
     * Используется если тип данных в onSuccess не важен
     */
    @PublishedApi
    internal suspend fun <@FunctionalityState State> executeOperationAndIgnoreDataFlow(
        call: suspend () -> Flow<State>,
        operationType: OperationType = OperationType.DefaultOperation,
        onSuccess: () -> Unit = {},
    ): Flow<State> {
        return executeEmptyOrWithDataIgnoringOperationFlow(
            call = call,
            operationType = operationType,
            onSuccess = { onSuccess() },
            onEmpty = {})
    }

    @OptIn(NotScreenState::class)
    private suspend fun <@FunctionalityState State> executeEmptyOrWithDataIgnoringOperationFlow(
        call: suspend () -> Flow<State>,
        operationType: OperationType = OperationType.DefaultOperation,
        onEmpty: () -> Unit = {},
        onSuccess: () -> Unit = {},
    ): Flow<State> {
        val mutableSharedFlow = call().shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly
        )
        if (_lastOperationStateWrapper.value.uiState is OperationState.Loading) {
            requestsQueue.offer(call.reflect()?.returnType.toString())
        }
        _lastOperationStateWrapper.value = StateWrapper(OperationState.Loading(operationType))
        // иначе код выполняется синхронно
        // и флоу вернется только когда весь этот участок будет пройден
        viewModelScope.launch {
            mutableSharedFlow.collect { requestResult ->
                if (requestResult is Unit) throw GenericsAutoCastIsWrong()
                val operationState = ToOperationStateMapper<State, Any>().map(requestResult)
                if (operationState is OperationState.Success) onSuccess.invoke()
                if (operationState is OperationState.Empty) onEmpty.invoke()
                _lastOperationStateWrapper.value = StateWrapper(operationState)
                pollFromQueueForFlow(operationState)
                showLoadingForFlowIfNeed(requestResult, operationType)
            }
        }
        return mutableSharedFlow
    }

    // значит что конечный резульатат получен и можно очистить очередь
    private fun pollFromQueueForFlow(operationState: OperationState<Any>) {
        if (operationState !is OperationState.Loading && operationState !is OperationState.NoState) {
            requestsQueue.poll()
        }
    }

    private fun <State> showLoadingForFlowIfNeed(
        requestResult: State,
        operationType: OperationType
    ) {
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