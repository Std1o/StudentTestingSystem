package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import lilith.domain.SingleEventFlow
import lilith.presentation.viewmodel.StatesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import stdio.lilith.core.delegates.StateFlowVar.Companion.stateFlowVar
import student.testing.system.R
import student.testing.system.common.Constants
import student.testing.system.common.CourseReviewNavigation
import student.testing.system.domain.models.Test
import student.testing.system.domain.models.TestResult
import student.testing.system.domain.models.UserAnswer
import student.testing.system.domain.models.UserQuestion
import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.states.operationStates.protect
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.contentState.TestPassingContentState
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TestPassingViewModel @Inject constructor(
    private val repository: TestsRepository,
    @CourseReviewNavigation private val courseNavigator: AppNavigator,
) :
    StatesViewModel() {

    private val _contentState = MutableStateFlow(TestPassingContentState())
    val contentState = _contentState.asStateFlow()
    private var contentStateVar by stateFlowVar(_contentState)

    private val _testPassingState = SingleEventFlow<TestResult>()
    val testPassingState = _testPassingState.asSharedFlow()

    private lateinit var test: Test
    private var isUserModerator = false

    val userQuestions: ArrayList<UserQuestion> = arrayListOf()
    private val _snackbarEvent = SingleEventFlow<Int>()
    val snackbarFlow = _snackbarEvent.asSharedFlow()

    fun setInitialData(test: Test, isUserModerator: Boolean) {
        this.test = test
        this.isUserModerator = isUserModerator

        updateTestPassingContentState(0)
    }

    fun onNextQuestion() {
        val currentQuestion = test.questions[contentStateVar.position]
        if (!currentQuestion.answers.any { it.isRight }) {
            _snackbarEvent.tryEmit(R.string.error_select_answers)
            return
        }
        val userAnswers = arrayListOf<UserAnswer>()
        for (ans in currentQuestion.answers) {
            val ansCopy = ans.copy() // to avoid bugs with further test takings
            userAnswers += UserAnswer(ansCopy.id!!, ansCopy.isRight)
            ans.isRight = false
        }
        userQuestions += UserQuestion(currentQuestion.id!!, userAnswers)
        if (contentStateVar.position == test.questions.size - 1) {
            viewModelScope.launch {
                if (isUserModerator) {
                    executeOperation(
                        call = {
                            repository.calculateDemoResult(test.courseId, test.id, userQuestions)
                        },
                        type = TestResult::class
                    ) {
                        _testPassingState.tryEmit(it)
                        navigateToResult()
                    }
                } else {
                    executeEmptyOperation(
                        call = {
                            repository.calculateResult(test.id, test.courseId, userQuestions)
                        }
                    ) { getResult() }.protect()
                }
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

    private fun getResult() {
        viewModelScope.launch {
            executeOperation({ repository.getResult(test.id, test.courseId) }, TestResult::class) {
                _testPassingState.tryEmit(it)
                navigateToResult()
            }.protect()
        }
    }

    private fun updateTestPassingContentState(position: Int) {
        contentStateVar = contentStateVar.copy(
            question = test.questions[position].question,
            answers = test.questions[position].answers,
            position = position
        )
    }
}