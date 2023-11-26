package student.testing.system.data.source.impl

import godofappstates.data.dataSource.BaseRemoteDataSource
import student.testing.system.data.api.AuthApi
import student.testing.system.data.source.interfaces.AuthRemoteDataSource
import student.testing.system.domain.models.SignUpReq
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val authApi: AuthApi) :
    BaseRemoteDataSource(), AuthRemoteDataSource {

    override suspend fun auth(request: String) = executeOperation { authApi.auth(request) }
    override suspend fun signUp(request: SignUpReq) = executeOperation { authApi.signUp(request) }
}