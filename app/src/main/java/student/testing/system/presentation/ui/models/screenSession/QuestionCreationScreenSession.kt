package student.testing.system.presentation.ui.models.screenSession

import student.testing.system.annotations.ScreenSession
import student.testing.system.models.Answer
import student.testing.system.presentation.ui.models.RequiredFieldState

@ScreenSession
data class QuestionCreationScreenSession(
    val questionState: RequiredFieldState = RequiredFieldState(),
    val answers: List<Answer> = arrayListOf()
)