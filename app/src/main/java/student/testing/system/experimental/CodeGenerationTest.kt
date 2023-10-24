package student.testing.system.experimental

import godofappstates.presentation.stateWrapper.StateWrapper
import godofappstates.presentation.viewmodel.StatesViewModel
import student.testing.system.domain.states.OperationState

class CodeGenerationTestViewModel : StatesViewModel() {
    lateinit var stateWrapper: StateWrapper<OperationState<String>>

    init {
        
    }
}