package student.testing.system.presentation.ui.models

import student.testing.system.annotations.ContentState

/**
 * For saving inputted email and password after screen recreating
 */
@ContentState
data class LoginContentState(
    val emailContentState: EmailContentState = EmailContentState(),
    val passwordContentState: PasswordContentState = PasswordContentState()
)

/**
 * In order to EmailTextField couldn't change password value
 */
@ContentState
class EmailContentState(var email: String = "")

/**
 * In order to PasswordTextField couldn't change email value
 */
@ContentState
class PasswordContentState(var password: String = "", var isVisible: Boolean = false)