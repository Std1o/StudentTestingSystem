package student.testing.system.presentation.ui.models.screenSession

import stdio.godofappstates.annotations.ScreenSession
import student.testing.system.models.Question
import student.testing.system.presentation.ui.models.RequiredFieldState

@ScreenSession
data class TestCreationScreenSession(
    val testNameState: RequiredFieldState = RequiredFieldState(),
    val questions: ArrayList<Question> = arrayListOf(),
)