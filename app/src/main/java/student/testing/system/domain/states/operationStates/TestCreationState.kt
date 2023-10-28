package student.testing.system.domain.states.operationStates

import stdio.godofappstates.annotations.OperationState
import student.testing.system.models.Test

@OperationState
sealed interface TestCreationState<out R> {
    data class Created(val test: Test) : TestCreationState<Nothing>
    data object EmptyName : TestCreationState<Nothing>
    data object NoQuestions : TestCreationState<Nothing>
}