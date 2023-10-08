package student.testing.system.presentation.ui.stateWrapper

import student.testing.system.domain.states.RequestState

class StateWrapper<State>(operationState: State = RequestState.NoState as State) :
    OnReceiveListener {
    var uiState: State = operationState
        private set

    override fun onReceive() {
        uiState = RequestState.NoState as State
    }
}