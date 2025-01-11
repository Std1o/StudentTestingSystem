package student.testing.system.domain.webSockets

interface KtorWebsocketClient {
    suspend fun connect()
    fun reconnect()
    suspend fun stop()
    suspend fun send(message: String)
}