package student.testing.system.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import student.testing.system.api.models.AuthReq
import student.testing.system.api.models.CreateOperationReq
import student.testing.system.api.models.Token
import student.testing.system.models.User


interface MainService {

    @POST("auth/sign-in")
    suspend fun auth(@Header("accept") accept: String, @Header("Content-Type") contentType: String, @Body request: String): Response<Token>

    //@POST("operations/")
    //suspend fun createOperation(@Body request: CreateOperationReq): Response<CreateOperationReq>
}