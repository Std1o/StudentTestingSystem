package student.testing.system.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import godofappstates.presentation.viewmodel.StatesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.common.Constants.TEST_CREATION_NAVIGATION
import student.testing.system.common.formatToString
import student.testing.system.domain.usecases.CreateTestUseCase
import student.testing.system.domain.usecases.AddQuestionUseCase
import student.testing.system.domain.states.QuestionState
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.states.operationStates.TestCreationState
import student.testing.system.models.Answer
import student.testing.system.models.CourseResponse
import student.testing.system.models.Question
import student.testing.system.models.Test
import student.testing.system.models.TestCreationReq
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.screenSession.QuestionCreationScreenSession
import student.testing.system.presentation.ui.models.screenSession.TestCreationScreenSession
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TestCreationViewModel @Inject constructor(
    @Named(TEST_CREATION_NAVIGATION) private val appNavigator: AppNavigator,
    private val addQuestionUseCase: AddQuestionUseCase,
    private val createTestUseCase: CreateTestUseCase
) : StatesViewModel() {

    val courseFlow = MutableStateFlow(CourseResponse("", 0, "", "", listOf()))

    private val _questionState = MutableStateFlow<QuestionState>(QuestionState.NoState)
    val questionState = _questionState.asStateFlow()

    private val _testState = MutableStateFlow<TestCreationState<Test>>(OperationState.NoState)
    val testState = _testState.asStateFlow()

    var questionCreationScreenSession by mutableStateOf(QuestionCreationScreenSession())
        private set
    var testCreationScreenSession by mutableStateOf(TestCreationScreenSession())

    fun setCourse(course: CourseResponse) {
        viewModelScope.launch {
            courseFlow.tryEmit(course)
        }
    }

    fun navigateToQuestionCreation() {
        appNavigator.tryNavigateTo(Destination.QuestionCreationScreen())
    }

    /**
     * @return 0 if success added
     */
    fun addAnswer(answerStr: String): Int {
        if (answerStr.isEmpty()) return R.string.error_empty_field
        val answer = Answer(answerStr, false)
        if (questionCreationScreenSession.answers.contains(answer)) return R.string.duplicate_element
        questionCreationScreenSession = questionCreationScreenSession.copy(
            answers = listOf(*questionCreationScreenSession.answers.toTypedArray(), answer)
        )
        return 0
    }

    fun addQuestion(questionStr: String) {
        val question = Question(questionStr, questionCreationScreenSession.answers)
        val state = addQuestionUseCase(question)
        if (state is QuestionState.QuestionSuccess) {
            testCreationScreenSession.questions += question
        }
        _questionState.value = state
        if (state is QuestionState.QuestionSuccess) {
            questionCreationScreenSession = QuestionCreationScreenSession()
            appNavigator.tryNavigateBack(Destination.TestCreationScreen.fullRoute)
        }
    }

    fun createTest(courseId: Int) {
        viewModelScope.launch {
            val requestResult = executeOperation({
                createTestUseCase(
                    TestCreationReq(
                        courseId,
                        testCreationScreenSession.testNameState.fieldValue,
                        Date().formatToString("yyyy-MM-dd")!!,
                        testCreationScreenSession.questions
                    )
                )
            }, Test::class)
            println(requestResult)
            _testState.value = requestResult
            if (requestResult is OperationState.Success) {
                _testState.value = TestCreationState.Created(requestResult.data)
            }
        }
    }

    fun onQuestionStateReceived() {
        _questionState.value = QuestionState.NoState
    }

    fun onTestNameChanged() {
        _testState.value = OperationState.NoState
    }
}