package student.testing.system.data

import okhttp3.Interceptor
import okhttp3.Response
import student.testing.system.common.AccountSession

class OAuthInterceptor(private val accountSession: AccountSession) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val token = accountSession.token
        if (!token.isNullOrEmpty()) {
            val finalToken = "Bearer $token"
            request = request.newBuilder()
                .addHeader("Authorization", finalToken)
                .build()
        }
        return chain.proceed(request)
    }
}