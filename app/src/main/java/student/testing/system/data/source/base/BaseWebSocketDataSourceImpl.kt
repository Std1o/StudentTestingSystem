package student.testing.system.data.source.base

import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import student.testing.system.common.AccountSession
import student.testing.system.data.api.KtorWebsocketClientImpl
import student.testing.system.domain.webSockets.KtorWebsocketClient
import student.testing.system.domain.webSockets.WebsocketEvent
import student.testing.system.domain.webSockets.WebsocketEvents
import javax.inject.Inject

open class BaseWebSocketDataSourceImpl<Params : Any> @Inject constructor(private val accountSession: AccountSession) {

    private val gson = Gson()

    @Volatile
    private var connectionJob: Job? = null

    fun <Result> getData(
        url: String,
        params: Params,
        resultType: Class<Result>
    ): Flow<WebsocketEvent<Result>> = callbackFlow {
        connectionJob?.cancelAndJoin()

        var clientRef: KtorWebsocketClient? = null

        val callback = object : WebsocketEvents {
            override fun onReceive(data: String) {
                trySend(WebsocketEvent.Receive(gson.fromJson(data, resultType)))
            }

            override fun onConnected() {
                clientRef?.let { c ->
                    launch {
                        runCatching { c.send(gson.toJson(params)) }.onFailure {
                            trySend(
                                WebsocketEvent.Disconnected(it.message ?: "Unknown error")
                            )
                        }
                    }
                }
                trySend(WebsocketEvent.Connected)
            }

            override fun onDisconnected(reason: String) {
                trySend(WebsocketEvent.Disconnected(reason))
            }
        }

        clientRef = KtorWebsocketClientImpl(url, callback, accountSession)

        val connectJob = launch { clientRef?.connect() }
        connectionJob = connectJob

        awaitClose {
            connectJob.cancel()
            launch { clientRef?.stop() }
            clientRef = null
            if (connectionJob === connectJob) connectionJob = null
        }
    }

    fun closeConnection(): Job? = connectionJob?.also { it.cancel() }
}