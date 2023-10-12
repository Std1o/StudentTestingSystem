package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import student.testing.system.domain.addQuestion.AddQuestionUseCase
import student.testing.system.domain.addQuestion.QuestionState
import student.testing.system.models.Answer
import student.testing.system.models.CourseResponse
import student.testing.system.models.Question
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.stateWrapper.QuestionStateWrapper
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TestCreationViewModel @Inject constructor(
    @Named("TestCreationNavigation") private val appNavigator: AppNavigator,
    private val addQuestionUseCase: AddQuestionUseCase
) :
    ViewModel() {

    val courseFlow = MutableStateFlow(CourseResponse("", 0, "", "", listOf()))

    private val _questionStateWrapper =
        QuestionStateWrapper.mutableStateFlow<QuestionState>()
    val questionStateWrapper = _questionStateWrapper.asStateFlow()

    fun setCourse(course: CourseResponse) {
        viewModelScope.launch {
            courseFlow.tryEmit(course)
        }
    }

    fun navigateToQuestionCreation() {
        appNavigator.tryNavigateTo(Destination.QuestionCreationScreen())
    }

    private val questions: ArrayList<Question> = arrayListOf()
    val questionsFlow = MutableSharedFlow<ArrayList<Question>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val answersFlow = MutableStateFlow<List<Answer>>(emptyList())
    private var answers: List<Answer> = arrayListOf()
        set(value) {
            field = value
            answersFlow.value = value
        }

    /**
     * @return true if success added
     */
    fun addAnswer(answerStr: String): Boolean {
        if (answerStr.isEmpty()) return false
        answers = answers.toMutableList().apply {
            add(Answer(answerStr, false))
        }
        return true
    }

    fun addQuestion(questionStr: String) {
        val question = Question(questionStr, answers)
        val state = addQuestionUseCase(question)
        if (state is QuestionState.QuestionSuccess) {
            questions += question
            questionsFlow.tryEmit(questions)
        }
        _questionStateWrapper.value = QuestionStateWrapper(state)
    }
}