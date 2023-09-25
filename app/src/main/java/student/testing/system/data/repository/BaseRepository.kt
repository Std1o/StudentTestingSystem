package student.testing.system.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import student.testing.system.annotations.NotScreenState
import student.testing.system.common.Utils
import student.testing.system.domain.states.RequestState

open class BaseRepository {

    @OptIn(NotScreenState::class)
    suspend fun <T> apiCall(call: suspend () -> Response<T>): RequestState<T> {
        try {
            val response = withContext(Dispatchers.IO) { call() }
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return RequestState.Success(body)
                }
                return RequestState.Empty(response.code())
            }
            val errorMessage = Utils.encodeErrorCode(response.errorBody())
            return error(errorMessage, response.code())
        } catch (e: Exception) {
            return error(e.message ?: " ", -1)
        }
    }

    private fun <T> error(errorMessage: String, code: Int): RequestState<T> =
        RequestState.Error(errorMessage, code)
}