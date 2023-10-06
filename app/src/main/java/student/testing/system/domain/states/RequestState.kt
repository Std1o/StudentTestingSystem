package student.testing.system.domain.states

import student.testing.system.annotations.NotScreenState
import student.testing.system.domain.operationTypes.OperationType

/**
 * Don't use RequestState in the presentation layer
 * <p>
 *
 * This can be used starting from data source and ending with ViewModel (exclusively).
 * <p>
 *
 * Starting with the ViewModel, you must select either LoadableData or OperationState, or their derivatives.
 */
sealed interface RequestState<out R> : OperationState<R>, LoadableData<R> {
    object NoState : RequestState<Nothing>

    @NotScreenState
    data class Success<out T>(
        val data: T,
        val operationType: OperationType = OperationType.DefaultOperation
    ) : RequestState<T>

    /**
     * Must be converted to Success with own local value in ViewModel
     */
    data class Empty(
        val code: Int,
        val operationType: OperationType = OperationType.DefaultOperation
    ) : RequestState<Nothing>

    data class Error(val exception: String, val code: Int = -1) : RequestState<Nothing>

    data class Loading(val operationType: OperationType = OperationType.DefaultOperation) :
        RequestState<Nothing>
}