package student.testing.system.api

import student.testing.system.api.models.AuthReq
import student.testing.system.api.models.CreateOperationReq
import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService : MainService) {
    //suspend fun auth(request: AuthReq) = mainService.auth("application/json", "application/x-www-form-urlencoded", request)
    suspend fun createOperation(request: CreateOperationReq) = mainService.createOperation(request)
}