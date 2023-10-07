package student.testing.system.domain.states

import androidx.annotation.StringRes
import student.testing.system.annotations.IntermediateState
import student.testing.system.domain.operationTypes.OperationType

sealed interface ValidatableOperationState<out R> {
    data class ValidationError(
        @StringRes val messageResId: Int,
        val operationType: OperationType = OperationType.DefaultOperation
    ) : ValidatableOperationState<Nothing>

    @IntermediateState
    data class SuccessfulValidation(val operationType: OperationType = OperationType.DefaultOperation) :
        ValidatableOperationState<Nothing>
}