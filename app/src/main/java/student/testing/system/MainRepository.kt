package student.testing.system

import student.testing.system.api.MainRemoteData
import student.testing.system.api.models.AuthReq
import student.testing.system.api.models.CreateOperationReq
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val remoteData : MainRemoteData
) {

    suspend fun auth() = remoteData.auth("grant_type=&username=Stdio&password=pass&scope=&client_id=&client_secret=")
    //suspend fun auth() = remoteData.createOperation(CreateOperationReq("2022-02-05", "income", 32100f, "Работай пж"))
}