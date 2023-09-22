package student.testing.system.data.mapper

import student.testing.system.domain.DataState
import student.testing.system.domain.auth.AuthState

class DataStateToLoadingStateMapper<T>(
    private val onSuccess: (data: T) -> Unit
) :
    Mapper<DataState<T>, AuthState<T>> {

    override fun map(input: DataState<T>): AuthState<T> {
        return when (input) {
            is DataState.Initial -> {
                return AuthState.Initial
            }

            is DataState.Loading -> {
                return AuthState.Loading
            }

            is DataState.Success -> {
                onSuccess.invoke(input.data)
                return AuthState.Success(input.data)
            }

            is DataState.Error -> {
                return AuthState.Error(input.exception, input.code)
            }

            else -> return AuthState.Loading
        }
    }
}