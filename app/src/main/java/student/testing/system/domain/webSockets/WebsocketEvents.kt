package student.testing.system.domain.webSockets

interface WebsocketEvents{
    fun onReceive(data: String)
    fun onConnected()
    fun onDisconnected(reason: String)
}