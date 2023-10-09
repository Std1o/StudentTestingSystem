package student.testing.system.presentation.ui.stateWrapper

import student.testing.system.domain.states.OperationState

class StateWrapper<State>(operationState: State = OperationState.NoState as State) :
    OnReceiveListener {
    var uiState: State = operationState
        private set

    override fun onReceive() {
        uiState = OperationState.NoState as State
    }
}