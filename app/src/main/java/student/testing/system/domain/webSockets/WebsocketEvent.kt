package student.testing.system.domain.webSockets

sealed interface WebsocketEvent {
    data class Receive(val data: String) : WebsocketEvent
    data object Connected : WebsocketEvent
    data object Disconnected : WebsocketEvent
}