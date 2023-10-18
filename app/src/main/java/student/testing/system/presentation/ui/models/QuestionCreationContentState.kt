package student.testing.system.presentation.ui.models

import student.testing.system.annotations.ContentState
import student.testing.system.models.Answer

@ContentState
data class QuestionCreationContentState(
    val questionContentState: RequiredFieldContentState = RequiredFieldContentState(),
    val answers: List<Answer> = arrayListOf()
)