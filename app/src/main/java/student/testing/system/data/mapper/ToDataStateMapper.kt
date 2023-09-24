package student.testing.system.data.mapper

import student.testing.system.domain.states.DataState

class ToDataStateMapper<State, T> : Mapper<State, DataState<T>> {
    override fun map(input: State): DataState<T> = try {
        input as DataState<T>
    } catch (e: ClassCastException) {
        DataState.NoState
    }
}