package student.testing.system.domain.repository

import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.models.PrivateUser
import student.testing.system.models.SignUpReq

interface AuthRepository {
    suspend fun auth(request: String): OperationState<PrivateUser>
    suspend fun signUp(request: SignUpReq): OperationState<PrivateUser>
}