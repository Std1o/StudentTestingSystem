package student.testing.system.presentation.ui.screens.login

/**
 * For saving inputted email and password after screen recreating
 */
data class LoginContentState(
    val emailContentState: EmailContentState = EmailContentState(),
    val passwordContentState: PasswordContentState = PasswordContentState()
)

/**
 * In order to EmailTextField couldn't change password value
 */
class EmailContentState(var email: String = "")

/**
 * In order to PasswordTextField couldn't change email value
 */
class PasswordContentState(var password: String = "", var isVisible: Boolean = false)