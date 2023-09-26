package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import student.testing.system.annotations.NotScreenState
import student.testing.system.data.mapper.ToOperationStateMapper
import student.testing.system.domain.states.OperationState
import student.testing.system.domain.states.RequestState

/**
 * BaseViewModel is an abstract class that provides a base implementation for ViewModels in the app.
 * It contains a StateFlow that represents the current state of the UI, and a method for launching
 * requests and updating the UI state based on the response.
 *
 * @param T The type of data that the ViewModel will handle.
 */


//гипотетически от этого могут наследоваться: CourseAddingViewModel,
// CoursesViewModel (deleteCourse), ParticipantsViewModel, TestPassingViewModel, TestsViewModel
open class BaseViewModelNew<State, T> : ViewModel(), ResettableViewModel {

    private val _lastOperationState = MutableStateFlow<OperationState<T>>(RequestState.NoState)
    val lastOperationState: StateFlow<OperationState<T>> = _lastOperationState.asStateFlow()

    private val toOperationStateMapper =
        ToOperationStateMapper<State, T>()

    /**
     * Launches a request and updates the UI state based on the response.
     *
     * @param requestResult A Flow representing the result of the request
     * @param onSuccess An optional callback function that will be called with each DataState emitted by the request result.
     * @param holdSuccess uses together with onSuccess. As it used if operation updates ContentState
     */
    @OptIn(NotScreenState::class)
    protected suspend fun launchRequest(
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