package student.testing.system.domain.webSockets

sealed interface WebsocketEvent<out T> {
    data class Receive<T>(val data: T) : WebsocketEvent<T>
    data object Connected : WebsocketEvent<Nothing>
    data object Disconnected : WebsocketEvent<Nothing>
}