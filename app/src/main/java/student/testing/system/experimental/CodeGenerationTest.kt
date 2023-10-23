package student.testing.system.experimental

import godofappstates.presentation.stateWrapper.StateWrapper
import godofappstates.presentation.viewmodel.StatesViewModel
import student.testing.system.domain.states.OperationState

fun codeGenerationTest() {
    lateinit var stateWrapper: StateWrapper<OperationState<String>>
    lateinit var statesViewModel: StatesViewModel
}