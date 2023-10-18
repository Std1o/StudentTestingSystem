package student.testing.system.presentation.ui.models.screenSession

import student.testing.system.annotations.ScreenSession
import student.testing.system.models.Question
import student.testing.system.presentation.ui.models.RequiredFieldState

@ScreenSession
data class TestCreationScreenSession(
    val testNameState: RequiredFieldState = RequiredFieldState(),
    val questions: ArrayList<Question> = arrayListOf(),
)