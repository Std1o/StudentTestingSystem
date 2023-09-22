package student.testing.system.data.mapper

import student.testing.system.domain.DataState
import student.testing.system.domain.login.LoginState

class DataStateToLoadingStateMapper<T>(
    private val onSuccess: (data: T) -> Unit
) :
    Mapper<DataState<T>, LoginState<T>> {

    override fun map(input: DataState<T>): LoginState<T> {
        return when (input) {
            is DataState.Initial -> {
                return LoginState.Initial
            }

            is DataState.Loading -> {
                return LoginState.Loading
            }

            is DataState.Success -> {
                onSuccess.invoke(input.data)
                return LoginState.Success(input.data)
            }

            is DataState.Error -> {
                return LoginState.Error(input.exception, input.code)
            }

            else -> return LoginState.Loading
        }
    }
}