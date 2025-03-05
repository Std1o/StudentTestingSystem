package student.testing.system.domain.mapper

import student.testing.system.domain.states.operationStates.OperationState

abstract class OperationStateMapper<I, O> : Mapper<OperationState<I>, OperationState<O>> {
    override fun map(input: OperationState<I>): OperationState<O> =
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
            is OperationState.Success -> OperationState.Success(getSuccess(input))
        }

    protected abstract fun getSuccess(input: OperationState.Success<I>): O
}