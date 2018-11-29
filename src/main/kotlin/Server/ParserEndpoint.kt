package Server

import PdfParsing.PdfParser
import PdfParsing.ProgressReporter
import com.atilika.kuromoji.ipadic.Tokenizer
import com.google.gson.GsonBuilder
import org.springframework.web.socket.handler.AbstractWebSocketHandler
import java.io.IOException
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.TextMessage



class ParserEndpoint: AbstractWebSocketHandler() {
    @Throws(IOException::class)
    override fun handleTextMessage(session: WebSocketSession?, message: TextMessage?) {
        println("New Text Message Received")
        session!!.sendMessage(TextMessage("60"))
    }

    @Throws(IOException::class)
    override fun handleBinaryMessage(session: WebSocketSession?, message: BinaryMessage?) {
        if (message != null && session != null) {
            val tokenizer = Tokenizer()
            val bytes = ByteArray(message.payloadLength)
            message.payload.get(bytes, 0, message.payloadLength)
            val pdfParser = PdfParser()
            val progressReporter = ProgressReporter {
                session.sendMessage(TextMessage(toJson(ParsingResult(it.toInt()))))
            }
            val words = pdfParser.parseTextFromPdf(bytes, progressReporter)
                    .flatMap { tokenizer.tokenize(it) }
                    .map { Word(
                            it.baseForm,
                            it.pronunciation,
                            listOf(
                                    it.partOfSpeechLevel1,
                                    it.partOfSpeechLevel2,
                                    it.partOfSpeechLevel3,
                                    it.partOfSpeechLevel4
                            )
                    )}
            val wordsAsJson = toJson(ParsingResult(100, words))
            session.sendMessage(TextMessage(wordsAsJson))
        }
    }

    fun toJson(words: ParsingResult): String {
        val gsonBuilder = GsonBuilder()
        val gson = gsonBuilder.create()
        return gson.toJson(words)
    }
}