package student.testing.system.presentation.ui.stateWrappers

import student.testing.system.domain.states.RequestState

class UIStateWrapper<State, T>(operationState: State = RequestState.NoState as State) {
    var uiState: State = operationState
        private set

    fun onReceive() {
        uiState = RequestState.NoState as State
    }
}