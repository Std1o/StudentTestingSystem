package student.testing.system.presentation.ui.stateWrapper

import student.testing.system.domain.states.RequestState
import student.testing.system.presentation.ui.models.CoursesContentState
import kotlin.reflect.KClass
import kotlin.reflect.cast

// TODO remove T
class UIStateWrapper<State>(operationState: State = RequestState.NoState as State) :
    OnReceiveListener {
    var uiState: State = operationState
        private set

    var type: KClass<Any>? = null

    fun <T> getData(data: T): T {
        println(type)
        return type?.cast(data) as T
    }

    override fun onReceive() {
        uiState = RequestState.NoState as State
    }
}