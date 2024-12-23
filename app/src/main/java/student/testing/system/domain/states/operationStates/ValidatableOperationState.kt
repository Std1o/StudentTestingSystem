package student.testing.system.domain.states.operationStates

import androidx.annotation.StringRes
import stdio.lilith.annotations.StillLoading
import stdio.lilith.core.domain.OperationType
import stdio.lilith.annotations.OperationState

@OperationState
sealed interface ValidatableOperationState<out R> {
    data class ValidationError(
        @StringRes val messageResId: Int,
        val operationType: OperationType = OperationType.DefaultOperation
    ) : ValidatableOperationState<Nothing>

    @StillLoading
    data class SuccessfulValidation(val operationType: OperationType = OperationType.DefaultOperation) :
        ValidatableOperationState<Nothing>
}