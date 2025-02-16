package student.testing.system.data.source.impl

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import student.testing.system.data.api.KtorWebsocketClientImpl
import student.testing.system.data.source.interfaces.TestResultsRemoteDataSource
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.domain.webSockets.KtorWebsocketClient
import student.testing.system.domain.webSockets.WebsocketEvent
import student.testing.system.domain.webSockets.WebsocketEvents
import javax.inject.Inject
import kotlin.properties.Delegates

class TestResultsRemoteDataSourceImpl @Inject constructor() : TestResultsRemoteDataSource {

    var isConnected: Boolean by Delegates.observable(false) { _, _, new ->
        if (new) {
            coroutineScope.launch {
                val jsonObject = Gson().toJson(params)
                client.send(jsonObject)
            }
        }
    }
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    lateinit var params: TestResultsRequestParams
    lateinit var client: KtorWebsocketClient

    override fun getResults(
        testId: Int,
        courseId: Int,
        params: TestResultsRequestParams
    ): Flow<WebsocketEvent> = callbackFlow {
        this@TestResultsRemoteDataSourceImpl.params = params
        val callback = object : WebsocketEvents {
            override fun onReceive(data: String) {
                trySendBlocking(WebsocketEvent.Receive(data))
            }

            override fun onConnected() {
                isConnected = true
                trySendBlocking(WebsocketEvent.Connected)
            }

            override fun onDisconnected(reason: String) {
                trySendBlocking(WebsocketEvent.Disconnected)
            }
        }
        client = KtorWebsocketClientImpl(
            url = "wss://testingsystem.ru/tests/ws/results/$testId?course_id=$courseId",
            callback
        )
        client.connect()
        awaitClose {
            coroutineScope.launch {
                client.stop()
            }
        }
    }
}