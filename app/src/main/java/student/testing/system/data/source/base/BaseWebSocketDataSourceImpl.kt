package student.testing.system.data.source.base

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import student.testing.system.data.api.KtorWebsocketClientImpl
import student.testing.system.domain.webSockets.KtorWebsocketClient
import student.testing.system.domain.webSockets.WebsocketEvent
import student.testing.system.domain.webSockets.WebsocketEvents
import javax.inject.Inject
import kotlin.properties.Delegates

open class BaseWebSocketDataSourceImpl<Params : Any> @Inject constructor() {

    private var isConnected: Boolean by Delegates.observable(false) { _, _, new ->
        if (new) {
            coroutineScope.launch {
                val jsonObject = Gson().toJson(params)
                client.send(jsonObject)
            }
        }
    }
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private lateinit var params: Params
    private lateinit var client: KtorWebsocketClient

    fun <Result> getData(
        url: String,
        params: Params,
        resultType: Class<Result>
    ): Flow<WebsocketEvent<Result>> = callbackFlow {
        if (this@BaseWebSocketDataSourceImpl::client.isInitialized) {
            client.stop()
        }

        this@BaseWebSocketDataSourceImpl.params = params
        val callback = object : WebsocketEvents {
            override fun onReceive(data: String) {
                println(data)
                val dataJson = Gson().fromJson(
                    data,
                    resultType
                )
                trySendBlocking(WebsocketEvent.Receive(dataJson))
            }

            override fun onConnected() {
                isConnected = true
                trySendBlocking(WebsocketEvent.Connected)
            }

            override fun onDisconnected(reason: String) {
                trySendBlocking(WebsocketEvent.Disconnected(reason))
            }
        }
        client = KtorWebsocketClientImpl(
            url = url,
            callback
        )
        client.connect()
        awaitClose {
            coroutineScope.launch {
                client.stop()
            }
        }
    }

    fun closeConnection(): Job = coroutineScope.launch {
        client.stop()
        coroutineScope.cancel()
    }
}