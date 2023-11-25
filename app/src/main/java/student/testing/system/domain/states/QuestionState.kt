package student.testing.system.domain.states

sealed class QuestionState {
    data object NoState : QuestionState()
    data object QuestionSuccess : QuestionState()
    data object EmptyQuestion : QuestionState()
    data object NoCorrectAnswers : QuestionState()
}