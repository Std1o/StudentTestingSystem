package student.testing.system.presentation.ui.screens.login

/**
 * For saving inputted email and password after screen recreating
 */
data class LoginContentState(private var email: String = "", private var password: String = "") {

    /**
     * In order to EmailTextField couldn't change password value
     */
    inner class EmailContentState {
        fun getEmail() = email
        fun setEmail(newEmail: String) {
            email = newEmail
        }
    }

    /**
     * In order to PasswordTextField couldn't change email value
     */
    inner class PasswordContentState {
        fun getPassword() = password

        fun setPassword(newPassword: String) {
            password = newPassword
        }
    }
}