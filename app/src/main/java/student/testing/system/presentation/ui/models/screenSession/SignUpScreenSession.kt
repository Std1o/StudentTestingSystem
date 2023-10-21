package student.testing.system.presentation.ui.models.screenSession

import stdio.godofappstates.annotations.ScreenSession
import student.testing.system.presentation.ui.models.RequiredFieldState

// TODO replace with rememberSaveable
@ScreenSession
data class SignUpScreenSession(
    val nameState: RequiredFieldState = RequiredFieldState(),
    val emailState: EmailState = EmailState(),
    val passwordState: PasswordState = PasswordState()
)