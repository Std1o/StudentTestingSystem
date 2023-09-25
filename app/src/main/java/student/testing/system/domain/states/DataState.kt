package student.testing.system.domain.states

import androidx.annotation.StringRes
import student.testing.system.annotations.NotScreenState

/*
  If 50 interfaces are inherited from DataState,
  then 50 types of states can be substituted into method that accepts DataState,
  49 of which will be harmful on the LoginScreen for example.

  If DataState is inherited from 50 interfaces, then into 50 methods accepting any interface,
  it will always be possible to substitute DataState.
 */
sealed interface DataState<out R> : AuthState<R> {
    object NoState : DataState<Nothing>, AuthState<Nothing>

    @NotScreenState
    data class Success<out T>(val data: T) : DataState<T>, AuthState<T>

    @Deprecated("Make it a special case")
    data class Empty(val code: Int) : DataState<Nothing>, AuthState<Nothing>
    data class Error(val exception: String, val code: Int = -1) : DataState<Nothing>,
        AuthState<Nothing>

    @Deprecated("Make it a special case")
    data class ValidationError(@StringRes val messageResId: Int) : DataState<Nothing>,
        AuthState<Nothing>

    object Loading : DataState<Nothing>, AuthState<Nothing>
}