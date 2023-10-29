package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import stdio.godofappstates.core.delegates.StateFlowVar.Companion.stateFlowVar
import student.testing.system.common.makeOperation
import student.testing.system.domain.MainRepository
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.models.Test
import student.testing.system.models.TestResult
import student.testing.system.models.UserQuestion
import student.testing.system.presentation.ui.models.contentState.TestPassingContentState
import javax.inject.Inject

// TODO сделать поле StateFlow и убрать StateFlow с методов, либо написать, почему этого сделать нельзя
@HiltViewModel
class TestPassingViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    private val _contentState = MutableStateFlow(TestPassingContentState())
    val contentState = _contentState.asStateFlow()
    private var contentStateVar by stateFlowVar(_contentState)

    private lateinit var test: Test
    private var isUserModerator = false

    val userQuestions: ArrayList<UserQuestion> = arrayListOf()

    fun setInitialData(test: Test, isUserModerator: Boolean) {
        this.test = test
        this.isUserModerator = isUserModerator

        updateTestPassingContentState(0)
    }

    fun onNextQuestion() {
        updateTestPassingContentState(contentStateVar.position + 1)
    }

    fun calculateResult(testId: Int, courseId: Int): StateFlow<OperationState<Int>> {
        val stateFlow = MutableStateFlow<OperationState<Int>>(OperationState.Loading())
        viewModelScope.launch {
            val requestResult =
                makeOperation(repository.calculateResult(testId, courseId, userQuestions), 0)
            stateFlow.emit(requestResult)
        }
        return stateFlow
    }

    fun calculateDemoResult(courseId: Int, testId: Int): StateFlow<LoadableData<TestResult>> {
        val stateFlow = MutableStateFlow<LoadableData<TestResult>>(LoadableData.Loading())
        viewModelScope.launch {
            val requestResult = repository.calculateDemoResult(courseId, testId, userQuestions)
            stateFlow.emit(requestResult)
        }
        return stateFlow
    }

    fun getResult(testId: Int, courseId: Int): StateFlow<OperationState<TestResult>> {
        val stateFlow = MutableStateFlow<OperationState<TestResult>>(OperationState.Loading())
        viewModelScope.launch {
            val requestResult = repository.getResult(testId, courseId)
            stateFlow.emit(requestResult)
        }
        return stateFlow
    }

    private fun updateTestPassingContentState(position: Int) {
        contentStateVar = contentStateVar.copy(
            question = test.questions[position].question,
            answers = test.questions[position].answers,
            position = position
        )
    }
}