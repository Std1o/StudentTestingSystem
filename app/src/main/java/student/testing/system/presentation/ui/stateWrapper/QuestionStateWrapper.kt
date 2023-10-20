package student.testing.system.presentation.ui.stateWrapper

import com.stdio.godofappstates.presentation.stateWrapper.OnReceiveListener
import kotlinx.coroutines.flow.MutableStateFlow
import student.testing.system.domain.addQuestion.QuestionState

class QuestionStateWrapper<State>(questionState: State = QuestionState.NoState as State) :
    OnReceiveListener {
    var uiState: State = questionState
        private set

    override fun onReceive() {
        uiState = QuestionState.NoState as State
    }

    companion object {
        fun <State> mutableStateFlow(questionState: State = QuestionState.NoState as State) =
            MutableStateFlow(QuestionStateWrapper(questionState))
    }
}