package student.testing.system.presentation.ui.stateWrapper

import com.stdio.godofappstates.presentation.stateWrapper.OnReceiveListener
import kotlinx.coroutines.flow.MutableStateFlow
import student.testing.system.domain.states.OperationState

/**
 * Wrapper is necessary in order for state to be resettable
 */
class StateWrapper<State>(operationState: State = OperationState.NoState as State) :
    OnReceiveListener {
    var uiState: State = operationState
        private set

    /**
     * Used to reset state. You can use it as a SingleEvent.
     * However, you can reset the state later. For example, to reset the error in TextField's
     *
     * ```
     * onValueChange {}
     * ```
     */
    override fun onReceive() {
        uiState = OperationState.NoState as State
    }

    companion object {
        fun <State> mutableStateFlow(operationState: State = OperationState.NoState as State) =
            MutableStateFlow(StateWrapper(operationState))
    }
}