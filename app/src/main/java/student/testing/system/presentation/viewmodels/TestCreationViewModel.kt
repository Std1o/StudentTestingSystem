package student.testing.system.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.domain.addQuestion.AddQuestionUseCase
import student.testing.system.domain.addQuestion.QuestionState
import student.testing.system.models.Answer
import student.testing.system.models.CourseResponse
import student.testing.system.models.Question
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.QuestionCreationContentState
import student.testing.system.presentation.ui.models.TestCreationContentState
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

    var questionCreationContentState by mutableStateOf(QuestionCreationContentState())
    var testCreationContentState by mutableStateOf(TestCreationContentState())

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
        if (questionCreationContentState.answers.contains(answer)) return R.string.duplicate_element
        questionCreationContentState.answers.add(answer)
        return 0
    }

    fun addQuestion(questionStr: String) {
        val question = Question(questionStr, questionCreationContentState.answers)
        val state = addQuestionUseCase(question)
        if (state is QuestionState.QuestionSuccess) {
            testCreationContentState.questions += question
        }
        _questionStateWrapper.value = QuestionStateWrapper(state)
    }
}