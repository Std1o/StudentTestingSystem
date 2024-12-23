package student.testing.system.domain.states.operationStates

import stdio.lilith.annotations.OperationState
import stdio.lilith.annotations.SingleEvents
import student.testing.system.domain.models.TestResult

@SingleEvents
@OperationState
sealed interface TestPassingState<out R> {
    data object TestPassed : TestPassingState<TestResult>
    data object TestNotPassed : TestPassingState<TestResult>
}