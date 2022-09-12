package student.testing.system.domain.addQuestion

import student.testing.system.models.Question

sealed class QuestionState {
    object QuestionSuccess : QuestionState()
    object EmptyQuestion : QuestionState()
    object NoCorrectAnswers : QuestionState()
}