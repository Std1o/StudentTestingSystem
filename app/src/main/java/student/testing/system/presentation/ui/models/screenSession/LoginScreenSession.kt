package student.testing.system.presentation.ui.models.screenSession

import student.testing.system.annotations.ScreenSession

// TODO replace with rememberSaveable
/**
 * For saving inputted email and password after screen recreating
 */
@ScreenSession
data class LoginScreenSession(
    val emailState: EmailState = EmailState(),
    val passwordState: PasswordState = PasswordState()
)

/**
 * In order to EmailTextField couldn't change password value
 */
class EmailState(var email: String = "")

/**
 * In order to PasswordTextField couldn't change email value
 */
class PasswordState(var password: String = "", var isVisible: Boolean = false)