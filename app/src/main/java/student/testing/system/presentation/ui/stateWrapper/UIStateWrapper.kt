package student.testing.system.presentation.ui.stateWrapper

import student.testing.system.domain.states.RequestState

// TODO remove T
class UIStateWrapper<State, T>(operationState: State = RequestState.NoState as State) :
    OnReceiveListener {
    var uiState: State = operationState
        private set

    override fun onReceive() {
        uiState = RequestState.NoState as State
    }
}