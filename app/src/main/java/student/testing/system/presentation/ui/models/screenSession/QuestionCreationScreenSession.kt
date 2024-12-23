package student.testing.system.presentation.ui.models.screenSession

import stdio.lilith.annotations.ScreenSession
import student.testing.system.domain.models.Answer
import student.testing.system.presentation.ui.models.RequiredFieldState

@ScreenSession
data class QuestionCreationScreenSession(
    val questionState: RequiredFieldState = RequiredFieldState(),
    val answers: List<Answer> = arrayListOf()
)