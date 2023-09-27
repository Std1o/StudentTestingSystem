package student.testing.system.data.dataSource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import student.testing.system.annotations.NotScreenState
import student.testing.system.common.Utils
import student.testing.system.domain.operationTypes.OperationType
import student.testing.system.domain.states.RequestState

open class BaseRemoteDataSource {

    /**
     * Generates a RequestState that contains a limited set of states for any request.
     * <p>
     *
     * ```
     * ```
     *
     * In its turn, use cases generate special states some any functionality
     * <p>
     *
     * (For example: authorization with validation, registration with validation).
     */
    @OptIn(NotScreenState::class)
    suspend fun <T> apiCall(
        operationType: OperationType = OperationType.DefaultOperation,
        call: suspend () -> Response<T>
    ): RequestState<T> {
        try {
            val response = withContext(Dispatchers.IO) { call() }
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return RequestState.Success(body, operationType)
                }
                return RequestState.Empty(response.code(), operationType)
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