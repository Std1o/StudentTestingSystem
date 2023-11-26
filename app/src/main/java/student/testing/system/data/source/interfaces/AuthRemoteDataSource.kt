package student.testing.system.data.source.interfaces

import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.models.PrivateUser
import student.testing.system.domain.models.SignUpReq

interface AuthRemoteDataSource {
    suspend fun auth(request: String): OperationState<PrivateUser>
    suspend fun signUp(request: SignUpReq): OperationState<PrivateUser>
}