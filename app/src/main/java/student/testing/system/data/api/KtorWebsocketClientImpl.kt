package student.testing.system.data.api

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.json.JSONObject
import student.testing.system.common.AccountSession
import student.testing.system.domain.webSockets.KtorWebsocketClient
import student.testing.system.domain.webSockets.WebsocketEvents
import java.util.concurrent.TimeUnit

class KtorWebsocketClientImpl(
    private val url: String,
    private val listener: WebsocketEvents,
): KtorWebsocketClient {

    companion object {
        private const val RECONNECT_DELAY = 10_000L
        private val PING_INTERVAL = 5_000L

        private const val TAG = "WebSocketClient"
    }

    private val client = HttpClient(OkHttp) {
        defaultRequest {
            header(HttpHeaders.Authorization, "Bearer ${AccountSession.instance.token!!}")
        }

        engine {
            config {
                pingInterval(PING_INTERVAL, TimeUnit.MILLISECONDS)
            }
        }

        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
        install(WebSockets)
    }

    private val scope = CoroutineScope(Dispatchers.IO) + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            Log.d(TAG, "Error: ${throwable.message}")
        }

    private var job: Job? = null

    private var session: WebSocketSession? = null

    override suspend fun connect() {
        try {
            Log.d(TAG, "Connecting to websocket at $url...")

            session = client.webSocketSession(url)

            listener.onConnected()

            Log.i(TAG, "Connected to websocket at $url")

            session!!.incoming
                .receiveAsFlow()
                .filterIsInstance<Frame.Text>()
                .filterNotNull()
                .collect { data ->
                    val message = data.readText()
                    listener.onReceive(message)

                    Log.i(TAG, "Received message: $message")
                }
        } catch (e: Exception) {
            listener.onDisconnected(e.message ?: "Unknown error")

            Log.d(TAG, "Error: ${e.message}")

            reconnect()
        }
    }

    override fun reconnect() {
        job?.cancel()
        Log.d(TAG, "Reconnecting to websocket in ${RECONNECT_DELAY}ms...")

        job = scope.launch {
            stop()
            delay(RECONNECT_DELAY)
            connect()
        }
    }

    override suspend fun stop() {
        Log.d(TAG, "Closing websocket session...")
        session?.close()
        session = null
    }

    override suspend fun send(message: String) {
        Log.d(TAG, "Sending message: $message")

        session?.send(Frame.Text(message))
    }
}