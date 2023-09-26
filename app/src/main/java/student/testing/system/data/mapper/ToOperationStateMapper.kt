package student.testing.system.data.mapper

import student.testing.system.domain.states.RequestState

class ToOperationStateMapper<State, T> : Mapper<State, RequestState<T>> {
    @Suppress("UNCHECKED_CAST")
    override fun map(input: State): RequestState<T> = try {
        input as RequestState<T>
    } catch (e: ClassCastException) {
        RequestState.NoState
    }
}