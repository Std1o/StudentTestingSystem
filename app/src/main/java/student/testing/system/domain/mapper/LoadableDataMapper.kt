package student.testing.system.domain.mapper

import student.testing.system.domain.states.loadableData.LoadableData

abstract class LoadableDataMapper<I, O> : Mapper<LoadableData<I>, LoadableData<O>> {
    override fun map(input: LoadableData<I>): LoadableData<O> =
        when (input) {
            is LoadableData.NoState -> LoadableData.NoState
            is LoadableData.Empty204 -> LoadableData.Empty204(input.code, input.dataType)
            is LoadableData.Error -> LoadableData.Error(
                input.exception,
                input.code,
                input.dataType
            )

            is LoadableData.Loading -> LoadableData.Loading(input.dataType)
            is LoadableData.Success -> LoadableData.Success(getSuccess(input))
        }

    protected abstract fun getSuccess(input: LoadableData.Success<I>): O
}