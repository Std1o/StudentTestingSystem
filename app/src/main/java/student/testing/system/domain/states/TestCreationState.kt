package student.testing.system.domain.states

import student.testing.system.annotations.FunctionalityState

@FunctionalityState
sealed interface TestCreationState<out R> {
    data object ReadyForPublication : TestCreationState<Nothing>
    data object EmptyName : TestCreationState<Nothing>
    data object NoQuestions : TestCreationState<Nothing>
}