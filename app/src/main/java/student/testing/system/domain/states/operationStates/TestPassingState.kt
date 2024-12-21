package student.testing.system.domain.states.operationStates

import stdio.godofappstates.annotations.OperationState
import stdio.godofappstates.annotations.SingleEvents
import student.testing.system.domain.models.TestResult

@SingleEvents
@OperationState
sealed interface TestPassingState<out R> {
    data object TestPassed : TestPassingState<TestResult>
    data object TestNotPassed : TestPassingState<TestResult>
}