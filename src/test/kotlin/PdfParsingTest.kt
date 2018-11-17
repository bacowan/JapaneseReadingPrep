import PdfParsing.PdfParser
import org.junit.Assert
import org.junit.Test
import java.io.File

class PdfParsingTest {
    @Test
    fun parseJapaneseText() {
        val parser = PdfParser()
        val pdfFile = File(javaClass.getResource("bocchan.pdf").file)
        val pdfText = parser.translateTextFromPdf(pdfFile)
        Assert.assertTrue(pdfText.isNotEmpty())
    }
}