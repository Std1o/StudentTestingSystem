package student.testing.system.common

import student.testing.system.domain.models.PrivateUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountSession @Inject constructor() {
    var token: String? = null
        private set
    var userId: Int? = null
        private set
    var email: String? = null
        private set
    var username: String? = null
        private set

    fun save(user: PrivateUser) {
        token = user.token
        userId = user.id
        email = user.email
        username = user.username
    }

    fun clear() {
        token = null
        userId = null
        email = null
        username = null
    }
}