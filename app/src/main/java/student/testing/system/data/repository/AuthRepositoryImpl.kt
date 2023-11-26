package student.testing.system.data.repository

import student.testing.system.data.source.interfaces.AuthRemoteDataSource
import student.testing.system.domain.repository.AuthRepository
import student.testing.system.models.SignUpReq
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val remoteDataSource: AuthRemoteDataSource) :
    AuthRepository {

    override suspend fun auth(request: String) = remoteDataSource.auth(request)
    override suspend fun signUp(request: SignUpReq) = remoteDataSource.signUp(request)
}