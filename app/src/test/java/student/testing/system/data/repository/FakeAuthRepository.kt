package student.testing.system.data.repository

import student.testing.system.domain.repository.AuthRepository
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.models.PrivateUser
import student.testing.system.models.SignUpReq

class FakeAuthRepository : AuthRepository {

    data class MockedUser(val email: String, val password: String)

    override suspend fun auth(request: String): OperationState<PrivateUser> {
        val existingUsers = listOf(MockedUser("test@mail.ru", "pass"))
        for (user in existingUsers) {
            if (request.contains(user.email) && request.contains("password=${user.password}")) {
                return OperationState.Success(PrivateUser(1, "Ivan", user.email, "some_token"))
            }
        }
        return OperationState.Error("Incorrect username or password")
    }

    override suspend fun signUp(request: SignUpReq): OperationState<PrivateUser> {
        TODO("Not yet implemented")
    }
}