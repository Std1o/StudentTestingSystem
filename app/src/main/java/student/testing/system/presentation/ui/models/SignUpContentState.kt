package student.testing.system.presentation.ui.models

import student.testing.system.annotations.InteractivityState

@InteractivityState
data class SignUpContentState(
    val nameContentState: RequiredFieldContentState = RequiredFieldContentState(),
    val emailContentState: EmailContentState = EmailContentState(),
    val passwordContentState: PasswordContentState = PasswordContentState()
)