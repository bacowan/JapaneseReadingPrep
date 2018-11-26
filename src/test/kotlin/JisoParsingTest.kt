import DictionarySearching.JishoSearcher
import com.atilika.kuromoji.ipadic.Tokenizer
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class JishoParsingTest {

    lateinit var mockServer: MockWebServer

    @Before
    fun setup() {
        mockServer = MockWebServer()
        mockServer.start()
    }

    @Test
    fun parseJishoSearchResultTest() {
        val mockResponse = MockResponse()
                .setResponseCode(200)
                .setBody(getJson("valid_response.json"))
        mockServer.enqueue(mockResponse)

        val searcher = JishoSearcher()
        val result = searcher.searchMostLikely(Tokenizer().tokenize("猫").first())

        Assert.assertEquals(1, result.english.count())
        Assert.assertEquals("cat", result.english.first().meaning)
        Assert.assertEquals(1, result.english.first().partsOfSpeech.count())
        Assert.assertEquals("Noun", result.english.first().partsOfSpeech.first())
        Assert.assertTrue(result.isCommon)
        Assert.assertEquals("ねこ", result.reading)
        Assert.assertEquals("猫", result.word)
    }

    @After
    fun teardown() {
        mockServer.shutdown()
    }

    private fun getJson(filename: String): String {
        return javaClass.getResource("json/$filename").readText()
    }
}