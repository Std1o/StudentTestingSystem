package student.testing.system.domain.states

import androidx.annotation.StringRes
import student.testing.system.annotations.NotScreenState

/*
  In order to have a limited hierarchy of states, the general interface is inherited from the special ones.

  So, for each state we have a limited hierarchy: the state itself (parent) and DataState (child).

  Otherwise, there would be a DataState with a many children
  that could be substituted under the guise of a DataState into absolutely any generic or method
 */
sealed interface DataState<out R> : AuthState<R> {
    object NoState : DataState<Nothing>, AuthState<Nothing>

    @NotScreenState
    data class Success<out T>(val data: T) : DataState<T>, AuthState<T>

    /**
     * Must be converted to Success with own local value in ViewModel
     */
    data class Empty(val code: Int) : DataState<Nothing>, AuthState<Nothing>
    data class Error(val exception: String, val code: Int = -1) : DataState<Nothing>,
        AuthState<Nothing>

    @Deprecated("Make it a special case")
    data class ValidationError(@StringRes val messageResId: Int) : DataState<Nothing>,
        AuthState<Nothing>

    object Loading : DataState<Nothing>, AuthState<Nothing>
}