package student.testing.system.domain

import student.testing.system.domain.states.loadableData.LoadableData

abstract class LoadableDataListMapper<I, O> :
    Mapper<LoadableData<List<I>>, LoadableData<List<O>>> {
    override fun map(input: LoadableData<List<I>>): LoadableData<List<O>> =
        when (input) {
            is LoadableData.NoState -> LoadableData.NoState
            is LoadableData.Empty204 -> LoadableData.Empty204(input.code, input.dataType)
            is LoadableData.Error -> LoadableData.Error(
                input.exception,
                input.code,
                input.dataType
            )

            is LoadableData.Loading -> LoadableData.Loading(input.dataType)
            is LoadableData.Success -> LoadableData.Success(input.data.map(::getSuccess))
        }

    protected abstract fun getSuccess(input: I): O
}