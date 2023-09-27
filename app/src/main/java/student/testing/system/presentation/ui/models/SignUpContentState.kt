package student.testing.system.presentation.ui.models

import student.testing.system.annotations.ContentState

@ContentState
data class SignUpContentState(
    val nameContentState: NameContentState = NameContentState(),
    val emailContentState: EmailContentState = EmailContentState(),
    val passwordContentState: PasswordContentState = PasswordContentState()
)

@ContentState
class NameContentState(var name: String = "")