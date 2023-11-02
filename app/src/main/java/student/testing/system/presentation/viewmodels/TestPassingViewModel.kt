package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import godofappstates.presentation.viewmodel.StatesViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import stdio.godofappstates.core.delegates.StateFlowVar.Companion.stateFlowVar
import student.testing.system.R
import student.testing.system.common.Constants
import student.testing.system.common.makeOperation
import student.testing.system.domain.MainRepository
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.states.operationStates.protect
import student.testing.system.models.Test
import student.testing.system.models.TestResult
import student.testing.system.models.UserAnswer
import student.testing.system.models.UserQuestion
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.contentState.TestPassingContentState
import javax.inject.Inject
import javax.inject.Named

// TODO сделать поле StateFlow и убрать StateFlow с методов, либо написать, почему этого сделать нельзя
@HiltViewModel
class TestPassingViewModel @Inject constructor(
    private val repository: MainRepository,
    @Named(Constants.COURSE_REVIEW_NAVIGATION) private val courseNavigator: AppNavigator,
) :
    StatesViewModel() {

    private val _contentState = MutableStateFlow(TestPassingContentState())
    val contentState = _contentState.asStateFlow()
    private var contentStateVar by stateFlowVar(_contentState)

    private val _resultReviewChannel = Channel<TestResult>()
    val resultReviewFlow = _resultReviewChannel.receiveAsFlow()

    private lateinit var test: Test
    private var isUserModerator = false

    val userQuestions: ArrayList<UserQuestion> = arrayListOf()
    private val _snackbarChannel = Channel<Int>()
    val snackbarFlow = _snackbarChannel.receiveAsFlow()

    fun setInitialData(test: Test, isUserModerator: Boolean) {
        this.test = test
        this.isUserModerator = isUserModerator

        updateTestPassingContentState(0)
    }

    fun onNextQuestion() {
        val currentQuestion = test.questions[contentStateVar.position]
        if (!currentQuestion.answers.any { it.isRight }) {
            _snackbarChannel.trySend(R.string.error_select_answers)
            return
        }
        val userAnswers = arrayListOf<UserAnswer>()
        for (ans in currentQuestion.answers) {
            userAnswers += UserAnswer(ans.id!!, ans.isRight)
        }
        userQuestions += UserQuestion(currentQuestion.id!!, userAnswers)
        if (contentStateVar.position == test.questions.size - 1) {
            viewModelScope.launch {
                executeEmptyOperation({
                    repository.calculateResult(test.id, test.courseId, userQuestions)
                }) {
                    getResult()
                }.protect()
            }
        } else {
            updateTestPassingContentState(contentStateVar.position + 1)
        }
    }

    private fun navigateToResult() {
        courseNavigator.tryNavigateTo(
            Destination.ResultReviewScreen(),
            inclusive = true,
            popUpToRoute = Destination.TestPassingScreen()
        )
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

    private fun getResult() {
        viewModelScope.launch {
            executeOperation({ repository.getResult(test.id, test.courseId) }, TestResult::class) {
                _resultReviewChannel.trySend(it)
                navigateToResult()
            }.protect()
        }
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