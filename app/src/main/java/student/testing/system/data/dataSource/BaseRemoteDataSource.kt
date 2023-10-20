package student.testing.system.data.dataSource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import com.stdio.godofappstates.util.Utils
import student.testing.system.domain.dataTypes.DataType
import student.testing.system.domain.operationTypes.OperationType
import student.testing.system.domain.states.LoadableData
import student.testing.system.domain.states.OperationState

open class BaseRemoteDataSource {

    /**
     * Generates OperationState that contains a limited set of states for any request.
     *
     * ```
     * ```
     *
     * In its turn, use cases generate special states some any functionality
     *
     * (For example: authorization with validation, registration with validation).
     */
    suspend fun <T> executeOperation(
        operationType: OperationType = OperationType.DefaultOperation,
        call: suspend () -> Response<T>
    ): OperationState<T> {
        try {
            val response = withContext(Dispatchers.IO) { call() }
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return OperationState.Success(body, operationType)
                }
                return OperationState.Empty204(response.code(), operationType)
            }
            val errorMessage = Utils.encodeErrorCode(response.errorBody())
            return operationError(errorMessage, response.code())
        } catch (e: Exception) {
            return operationError(e.message ?: " ", -1)
        }
    }

    /**
     * Generates LoadableData that contains a limited set of loading data states
     */
    suspend fun <T> loadData(
        dataType: DataType = DataType.NotSpecified,
        call: suspend () -> Response<T>
    ): LoadableData<T> {
        try {
            val response = withContext(Dispatchers.IO) { call() }
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return LoadableData.Success(body, dataType)
                }
                return LoadableData.Empty204(response.code(), dataType)
            }
            val errorMessage = Utils.encodeErrorCode(response.errorBody())
            return loadError(errorMessage, response.code())
        } catch (e: Exception) {
            return loadError(e.message ?: " ", -1)
        }
    }

    private fun <T> operationError(errorMessage: String, code: Int): OperationState<T> =
        OperationState.Error(errorMessage, code)

    private fun <T> loadError(errorMessage: String, code: Int): LoadableData<T> =
        LoadableData.Error(errorMessage, code)
}