package student.testing.system.domain.addQuestion

import student.testing.system.models.Question

sealed class QuestionState {
    data class QuestionSuccess(val data: Question) : QuestionState()
    object EmptyQuestion : QuestionState()
    object NoCorrectAnswers : QuestionState()
}