package student.testing.system.domain.states

/**
 * OperationState contains the result of operation and is not intended for long-term storage of the screen state.
 *
 * Its value must either be passed to the ContentState,
 * or used for some quick actions (show the loader, show the snackbar with an error, navigate to a new screen)
 */
sealed interface OperationState<out R> : AuthState<R>