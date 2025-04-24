package student.testing.system.data.repository

import student.testing.system.data.mapper.PrivateUserMapper
import student.testing.system.data.source.interfaces.AuthRemoteDataSource
import student.testing.system.domain.models.SignUpReq
import student.testing.system.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val remoteDataSource: AuthRemoteDataSource) :
    AuthRepository {

    override suspend fun auth(request: String) =
        PrivateUserMapper().map(remoteDataSource.auth(request))

    override suspend fun signUp(request: SignUpReq) =
        PrivateUserMapper().map(remoteDataSource.signUp(request))
}