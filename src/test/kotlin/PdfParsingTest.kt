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
        Assert.assertEquals(5, pdfText.count())
        Assert.assertTrue(pdfText[0].isNotBlank())
        // don't expect anything from pdfText[1]
        Assert.assertTrue(pdfText[2].isNotBlank())
        Assert.assertTrue(pdfText[3].isNotBlank())
        Assert.assertTrue(pdfText[4].isNotBlank())
    }
}