package student.testing.system.presentation.ui.models.contentState

import student.testing.system.domain.models.Answer

data class TestPassingContentState(
    val question: String = "",
    val answers: List<Answer> = emptyList(),
    val position: Int = 0
)
