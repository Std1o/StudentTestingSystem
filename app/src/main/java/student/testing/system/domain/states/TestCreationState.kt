package student.testing.system.domain.states

import student.testing.system.annotations.FunctionalityState
import student.testing.system.models.Test

@FunctionalityState
sealed interface TestCreationState<out R> {
    data class ReadyForPublication(val test: Test) : TestCreationState<Nothing>
    data object EmptyName : TestCreationState<Nothing>
    data object NoQuestions : TestCreationState<Nothing>
}