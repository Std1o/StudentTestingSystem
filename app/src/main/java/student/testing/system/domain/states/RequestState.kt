package student.testing.system.domain.states

import androidx.annotation.StringRes
import student.testing.system.annotations.NotScreenState

/*
  In order to have a limited hierarchy of states, the general interface is inherited from the special ones.

  So, for each state we have a limited hierarchy: the state itself (parent) and DataState (child).

  Otherwise, there would be a DataState with a many children
  that could be substituted under the guise of a DataState into absolutely any generic or method
 */

/**
 * DataState contains the result of request and is not intended for long-term storage of the screen state.
 *
 * Its value must either be passed to the UI state,
 * or used for some quick actions (show the loader, show the snackbar with an error, navigate to a new screen)
 */
sealed interface RequestState<out R> : OperationState<R>, LoadableData<R> {
    object NoState : RequestState<Nothing>

    @NotScreenState
    data class Success<out T>(val data: T) : RequestState<T>

    /**
     * Must be converted to Success with own local value in ViewModel
     */
    data class Empty(val code: Int) : RequestState<Nothing>
    data class Error(val exception: String, val code: Int = -1) : RequestState<Nothing>

    @Deprecated("Make it a special case")
    data class ValidationError(@StringRes val messageResId: Int) : RequestState<Nothing>

    object Loading : RequestState<Nothing>
}