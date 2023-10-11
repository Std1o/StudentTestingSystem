package student.testing.system.presentation.ui.models

import student.testing.system.annotations.ContentState

@ContentState
data class SignUpContentState(
    val nameContentState: RequiredFieldContentState = RequiredFieldContentState(),
    val emailContentState: EmailContentState = EmailContentState(),
    val passwordContentState: PasswordContentState = PasswordContentState()
)