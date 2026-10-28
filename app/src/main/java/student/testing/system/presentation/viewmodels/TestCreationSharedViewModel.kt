package student.testing.system.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import lilith.presentation.viewmodel.StatesViewModel
import student.testing.system.R
import student.testing.system.common.TestCreationNavigation
import student.testing.system.common.formatToString
import student.testing.system.domain.models.*
import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.states.QuestionState
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.states.operationStates.TestCreationState
import student.testing.system.domain.usecases.AddQuestionUseCase
import student.testing.system.domain.usecases.CreateTestUseCase
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.RequiredFieldState
import student.testing.system.presentation.ui.models.screenSession.QuestionCreationScreenSession
import student.testing.system.presentation.ui.models.screenSession.TestCreationScreenSession
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TestCreationSharedViewModel @Inject constructor(
    @TestCreationNavigation private val appNavigator: AppNavigator,
    private val addQuestionUseCase: AddQuestionUseCase,
    private val createTestUseCase: CreateTestUseCase,
    private val testRepository: TestsRepository
) : StatesViewModel() {

    val courseFlow = MutableStateFlow(Course("", 0, "", "", listOf()))

    private val _questionState = MutableStateFlow<QuestionState>(QuestionState.NoState)
    val questionState = _questionState.asStateFlow()

    private val _testState = MutableStateFlow<TestCreationState<Test>>(OperationState.NoState)
    val testState = _testState.asStateFlow()

    var questionCreationScreenSession by mutableStateOf(QuestionCreationScreenSession())
        private set
    var testCreationScreenSession by mutableStateOf(TestCreationScreenSession())

    private lateinit var aiQuestionJob: Job

    var testName by mutableStateOf("")
        private set

    var questionStr by mutableStateOf("")
        private set

    fun setCourse(course: Course) {
        viewModelScope.launch {
            courseFlow.tryEmit(course)
        }
    }

    fun onTestNameChanged(newTestName: String) {
        testName = newTestName
    }

    fun onQuestionStrChange(newQuestionStr: String) {
        questionStr = newQuestionStr
    }

    fun navigateToQuestionCreation() {
        appNavigator.tryNavigateTo(Destination.QuestionCreationScreen())
    }

    private val _answers = MutableStateFlow<List<Answer>>(emptyList())

    val answers = _answers.asStateFlow()

    fun toggleAnswer(answer: Answer) {
        _answers.update { currentList ->
            currentList.map {
                if (it.answer == answer.answer) {
                    it.copy(isRight = !it.isRight)
                } else {
                    it
                }
            }
        }
    }

    fun getAIQuestion() {
        aiQuestionJob = viewModelScope.launch {
            if (isActive) {
                val response =
                    executeOperation(
                        call = { testRepository.getAIQuestion(questionStr) },
                        String::class
                    )
                if (response is OperationState.Success) {
                    questionCreationScreenSession =
                        questionCreationScreenSession.copy(
                            questionState = RequiredFieldState(
                                fieldValue = response.data
                            )
                        )
                }
            }
        }
    }

    fun cancelQuestionGeneration() {
        aiQuestionJob.cancel()
        clearOperationState()
        clearEvents()
    }

    /**
     * @return 0 if success added
     */
    fun addAnswer(answerStr: String): Int {
        if (answerStr.isEmpty()) return R.string.error_empty_field
        val answer = Answer(answerStr, false)
        if (_answers.value.contains(answer)) return R.string.duplicate_element
        _answers.value = listOf(*_answers.value.toTypedArray(), answer)
        return 0
    }

    fun addQuestion() {
        val question = Question(questionStr, answers.value)
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
                        testName,
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