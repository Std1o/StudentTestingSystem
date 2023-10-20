package student.testing.system.domain.states

import androidx.annotation.StringRes
import com.stdio.godofappstates.annotations.StillLoading
import student.testing.system.domain.operationTypes.OperationType

sealed interface ValidatableOperationState<out R> {
    data class ValidationError(
        @StringRes val messageResId: Int,
        val operationType: OperationType = OperationType.DefaultOperation
    ) : ValidatableOperationState<Nothing>

    @StillLoading
    data class SuccessfulValidation(val operationType: OperationType = OperationType.DefaultOperation) :
        ValidatableOperationState<Nothing>
}