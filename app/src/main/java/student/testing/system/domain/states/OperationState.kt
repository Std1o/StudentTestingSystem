package student.testing.system.domain.states

import stdio.godofappstates.annotations.FunctionalityState
import com.stdio.godofappstates.domain.OperationType

/**
 * OperationState contains the result of operation and is not intended for long-term storage of the screen state.
 *
 * Its value must either be passed to the ContentState,
 * or used for some quick actions (show the loader, show the snackbar with an error, navigate to a new screen)
 */
@FunctionalityState
sealed interface OperationState<out R> : ValidatableOperationState<R>, AuthState<R>,
    TestCreationState<R> {
    data object NoState : OperationState<Nothing>

    data class Success<out T>(
        val data: T,
        val operationType: OperationType = OperationType.DefaultOperation
    ) : OperationState<T>

    /**
     * Must be converted to Success with own local value in ViewModel.
     *
     * Or you have to handle it by yourself
     */
    data class Empty204(
        val code: Int,
        val operationType: OperationType = OperationType.DefaultOperation
    ) : OperationState<Nothing>

    data class Error(
        val exception: String,
        val code: Int = -1,
        val operationType: OperationType = OperationType.DefaultOperation
    ) : OperationState<Nothing>

    data class Loading(val operationType: OperationType = OperationType.DefaultOperation) :
        OperationState<Nothing>
}