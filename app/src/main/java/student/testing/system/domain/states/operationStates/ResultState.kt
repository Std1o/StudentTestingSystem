package student.testing.system.domain.states.operationStates

import stdio.lilith.annotations.OperationState

@OperationState
sealed interface ResultState<out R> {
    data object NoResult : ResultState<Nothing>
}