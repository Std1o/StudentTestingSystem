package student.testing.system.domain.states

import stdio.godofappstates.annotations.OperationState

@OperationState
sealed interface ResultState<out R> {
    data object NoResult : ResultState<Nothing>
}