package student.testing.system.domain.usecases

import student.testing.system.domain.states.QuestionState
import student.testing.system.domain.models.Question
import javax.inject.Inject

class AddQuestionUseCase @Inject constructor() {

    operator fun invoke(question: Question): QuestionState {
        if (question.question.isEmpty()) {
            return QuestionState.EmptyQuestion
        }
        for (ans in question.answers) {
            if (ans.isRight) {
                return QuestionState.QuestionSuccess
            }
        }
        return QuestionState.NoCorrectAnswers
    }
}