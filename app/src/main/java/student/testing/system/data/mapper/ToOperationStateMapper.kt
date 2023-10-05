package student.testing.system.data.mapper

import student.testing.system.domain.states.RequestState

class ToOperationStateMapper<T> : Mapper<Any, RequestState<T>> {
    @Suppress("UNCHECKED_CAST")
    override fun map(input: Any): RequestState<T> = try {
        input as RequestState<T>
    } catch (e: ClassCastException) {
        RequestState.NoState
    }
}