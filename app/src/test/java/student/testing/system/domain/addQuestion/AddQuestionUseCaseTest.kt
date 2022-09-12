package student.testing.system.domain.addQuestion

import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Test
import student.testing.system.R
import student.testing.system.domain.login.AuthUseCase
import student.testing.system.domain.login.LoginState
import student.testing.system.models.Answer
import student.testing.system.models.Question

class AddQuestionUseCaseTest {

    private val addQuestionUseCase = spyk(AddQuestionUseCase())

    @Test
    fun `empty question returns EmptyQuestion state`() {
        val expected = QuestionState.EmptyQuestion
        val actual = addQuestionUseCase(Question("", emptyList()))
        assertEquals(expected, actual)
    }

    @Test
    fun `empty answers returns NoCorrectAnswers state`() {
        val expected = QuestionState.NoCorrectAnswers
        val actual = addQuestionUseCase(Question("Some question", emptyList()))
        assertEquals(expected, actual)
    }

    @Test
    fun `if there are no correct answers return NoCorrectAnswers state`() {
        val answers = listOf(Answer("Ans 1", false), Answer("Ans 2", false))
        val expected = QuestionState.NoCorrectAnswers
        val actual = addQuestionUseCase(Question("Some question", answers))
        assertEquals(expected, actual)
    }

    @Test
    fun `if at least one answer is right return QuestionSuccess`() {
        val answers = listOf(Answer("Ans 1", false), Answer("Ans 2", true))
        val expected = QuestionState.QuestionSuccess
        val actual = addQuestionUseCase(Question("Some question", answers))
        assertEquals(expected, actual)
    }
}