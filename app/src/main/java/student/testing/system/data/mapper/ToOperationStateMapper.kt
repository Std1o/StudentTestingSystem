package student.testing.system.data.mapper

import student.testing.system.domain.states.OperationState

class ToOperationStateMapper<State, T> {
    @Suppress("UNCHECKED_CAST")
    fun map(input: State): OperationState<T> = try {
        input as OperationState<T>
    } catch (e: ClassCastException) {
        OperationState.NoState
    }
}