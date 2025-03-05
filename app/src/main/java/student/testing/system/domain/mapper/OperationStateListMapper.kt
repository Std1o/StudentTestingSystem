package student.testing.system.domain.mapper

import student.testing.system.domain.states.operationStates.OperationState

abstract class OperationStateListMapper<I, O> :
    Mapper<OperationState<List<I>>, OperationState<List<O>>> {
    override fun map(input: OperationState<List<I>>): OperationState<List<O>> =
        when (input) {
            is OperationState.NoState -> OperationState.NoState
            is OperationState.Empty204 -> OperationState.Empty204(input.code, input.operationType)
            is OperationState.Error -> OperationState.Error(
                input.exception,
                input.code,
                input.operationType
            )

            is OperationState.ErrorSingle -> OperationState.ErrorSingle(
                input.exception,
                input.code,
                input.operationType
            )

            is OperationState.Loading -> OperationState.Loading(input.operationType)
            is OperationState.Success -> OperationState.Success(input.data.map(::getSuccess))
        }

    protected abstract fun getSuccess(input: I): O
}