package student.testing.system.data.mapper

import student.testing.system.data.dto.PrivateUserDTO
import student.testing.system.domain.OperationStateMapper
import student.testing.system.domain.models.PrivateUser
import student.testing.system.domain.states.operationStates.OperationState

class PrivateUserMapper : OperationStateMapper<PrivateUserDTO, PrivateUser>() {
    override fun getSuccess(input: OperationState.Success<PrivateUserDTO>): PrivateUser =
        PrivateUser(
            id = input.data.id,
            username = input.data.username,
            email = input.data.email,
            token = input.data.token
        )
}