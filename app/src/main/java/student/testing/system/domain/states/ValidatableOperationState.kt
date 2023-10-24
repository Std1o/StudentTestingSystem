package student.testing.system.domain.states

import androidx.annotation.StringRes
import stdio.godofappstates.annotations.StillLoading
import stdio.godofappstates.core.domain.OperationType
import stdio.godofappstates.annotations.OperationState

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