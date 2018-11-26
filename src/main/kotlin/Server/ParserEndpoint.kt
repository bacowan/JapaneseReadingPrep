package Server

import org.springframework.web.socket.handler.AbstractWebSocketHandler
import java.io.IOException
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.TextMessage



class ParserEndpoint: AbstractWebSocketHandler() {
    @Throws(IOException::class)
    override fun handleTextMessage(session: WebSocketSession?, message: TextMessage?) {
        println("New Text Message Received")
        session!!.sendMessage(message)
    }

    @Throws(IOException::class)
    override fun handleBinaryMessage(session: WebSocketSession?, message: BinaryMessage?) {
        println("New Binary Message Received")
        session!!.sendMessage(message)
    }
}