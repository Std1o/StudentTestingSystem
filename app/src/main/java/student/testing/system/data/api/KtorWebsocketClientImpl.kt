package student.testing.system.data.api

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
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
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import student.testing.system.common.AccountSession
import student.testing.system.domain.webSockets.KtorWebsocketClient
import student.testing.system.domain.webSockets.WebsocketEvents
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.cancellation.CancellationException

class KtorWebsocketClientImpl(
    private val url: String,
    private val listener: WebsocketEvents,
    private val accountSession: AccountSession,
) : KtorWebsocketClient {

    companion object {
        private const val RECONNECT_DELAY = 10_000L
        private const val PING_INTERVAL = 5_000L
        private const val TAG = "WebSocketClient"
    }

    private val isReconnecting = AtomicBoolean(false)

    private val client = HttpClient(OkHttp) {
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
    }.apply {
        // Перехватываем КАЖДЫЙ запрос — читаем токен в момент отправки
        plugin(HttpSend).intercept { request ->
            accountSession.token
                ?.takeIf { it.isNotBlank() }
                ?.let { token ->
                    request.headers.append(
                        HttpHeaders.Authorization,
                        "Bearer $token"
                    )
                }
            execute(request)
        }
    }

    private val scope =
        CoroutineScope(Dispatchers.IO) + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            Log.d(TAG, "Error: ${throwable.message}")
        }

    private var job: Job? = null
    private var session: WebSocketSession? = null

    override suspend fun connect(): Unit = withContext(Dispatchers.IO) {
        if (isReconnecting.compareAndSet(false, true)) {
            job = scope.launch {
                try {
                    while (true) {
                        try {
                            Log.d(TAG, "Connecting to websocket at $url...")
                            session = client.webSocketSession(url)
                            listener.onConnected()
                            isReconnecting.set(false)

                            Log.i(TAG, "Connected to websocket at $url")

                            session?.incoming
                                ?.receiveAsFlow()
                                ?.filterIsInstance<Frame.Text>()
                                ?.collect { data ->
                                    val message = data.readText()
                                    listener.onReceive(message)
                                    Log.i(TAG, "Received message: $message")
                                }
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            listener.onDisconnected(e.message ?: "Unknown error")
                            Log.d(TAG, "Error: ${e.message}")
                            isReconnecting.set(true)
                            runCatching { session?.close() }
                            session = null
                        }

                        Log.d(TAG, "Reconnecting in ${RECONNECT_DELAY}ms...")
                        delay(RECONNECT_DELAY)
                    }
                } finally {
                    isReconnecting.set(false)
                }
            }
        } else {
            Log.d(TAG, "connect() skipped: already connected or connecting")
        }
    }

    override fun reconnect() {
        scope.launch {
            job?.cancelAndJoin()
            connect()
        }
    }

    override suspend fun stop() {
        Log.d(TAG, "Closing websocket session...")
        job?.cancelAndJoin()
        job = null
        runCatching { session?.close() }
        session = null
    }

    override suspend fun send(message: String) {
        Log.d(TAG, "Sending message: $message")
        session?.send(Frame.Text(message))
    }
}