package student.testing.system.presentation.ui.models

import student.testing.system.annotations.ContentState
import student.testing.system.models.Answer
import student.testing.system.models.Question

@ContentState
data class QuestionCreationContentState(
    val questionContentState: RequiredFieldContentState = RequiredFieldContentState(),
    val answers: ArrayList<Answer> = arrayListOf()
)