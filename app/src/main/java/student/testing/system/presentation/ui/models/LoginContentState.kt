package student.testing.system.presentation.ui.models

import student.testing.system.annotations.ScreenSession

/**
 * For saving inputted email and password after screen recreating
 */
@ScreenSession
data class LoginContentState(
    val emailContentState: EmailContentState = EmailContentState(),
    val passwordContentState: PasswordContentState = PasswordContentState()
)

/**
 * In order to EmailTextField couldn't change password value
 */
@ScreenSession
class EmailContentState(var email: String = "")

/**
 * In order to PasswordTextField couldn't change email value
 */
@ScreenSession
class PasswordContentState(var password: String = "", var isVisible: Boolean = false)