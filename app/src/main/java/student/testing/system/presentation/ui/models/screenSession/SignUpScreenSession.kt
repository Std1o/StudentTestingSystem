package student.testing.system.presentation.ui.models.screenSession

import student.testing.system.annotations.ScreenSession
import student.testing.system.presentation.ui.models.RequiredFieldState

@ScreenSession
data class SignUpScreenSession(
    val nameState: RequiredFieldState = RequiredFieldState(),
    val emailState: EmailState = EmailState(),
    val passwordState: PasswordState = PasswordState()
)