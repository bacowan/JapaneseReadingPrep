package PdfParsing

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.apache.pdfbox.text.PDFTextStripper
import org.bytedeco.javacpp.BytePointer
import org.bytedeco.javacpp.lept
import org.bytedeco.javacpp.lept.pixDestroy
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import javax.imageio.ImageIO
import org.bytedeco.javacpp.lept.*
import org.bytedeco.javacpp.tesseract.*

class PdfParser {

    private val japanesePosToEnglish = hashMapOf(
            "名詞" to listOf("Noun, Place"),
            "動詞" to listOf("Verb"),
            "形容詞" to listOf("Adjective"),
            "副詞" to listOf("Adverb"),
            "助詞" to listOf("Particle"),
            "接続詞" to listOf("Conjugation"),
            "助動詞" to listOf("Pronoun"),
            "連体詞" to listOf("Pre-noun adjectival", "I-adjective", "No-adjective"),
            "感動詞" to listOf("Interjection")
    )

    fun parseTextFromPdf(pdfFile: File, progress: ProgressReporter = ProgressReporter{}): List<String> {
        return parseTextFromPdf(pdfFile.readBytes(), progress)
    }

    fun parseTextFromPdf(pdfFile: ByteArray, progress: ProgressReporter = ProgressReporter{}): List<String> {
        val textPages = PDDocument.load(pdfFile).use {
            progress.setAsRatio(0, it.numberOfPages)
            extractTextFromPdf(it, progress)
        }
        return textPages.map { singleLine(it) }
    }

    private fun extractTextFromPdf(doc: PDDocument, progress: ProgressReporter = ProgressReporter{}): List<String> {
        val pdfStripper = PDFTextStripper()
        val pdfRenderer = PDFRenderer(doc)
        val ret = mutableListOf<String>()
        (0 until doc.numberOfPages).forEach { pageNumber ->
            pdfStripper.startPage = pageNumber
            pdfStripper.endPage = pageNumber
            var text = pdfStripper.getText(doc)
            if (text.trim().isEmpty()) {
                text = getTextFromImage(pdfRenderer.renderImage(pageNumber))
            }
            ret.add(text)
            progress.increment()
        }
        return ret
    }

    private fun singleLine(page: String): String {
        return page.replace(System.lineSeparator().toRegex(), "")
    }

    private fun getTextFromImage(image: BufferedImage): String {
        val api = TessBaseAPI()
        var pixImage: lept.PIX? = null
        var result: BytePointer? = null
        try {
            if (api.Init("src/main/resources/tessdata", "jpn_vert") != 0) {
                throw InitializationException("Could not initialize tesseract OCR library.")
            }
            api.SetPageSegMode(PSM_SINGLE_BLOCK_VERT_TEXT)
            val baos = ByteArrayOutputStream()
            ImageIO.write(image, "bmp", baos)
            val bytes = baos.toByteArray()
            baos.close()
            pixImage = pixReadMem(BytePointer(ByteBuffer.wrap(bytes)), bytes.size.toLong())
            api.SetImage(pixImage)
            result = api.GetUTF8Text()
            return result?.string ?: ""
        } finally {
            api.End()
            if (pixImage != null) {
                pixDestroy(pixImage)
            }
            result?.deallocate()
        }
    }
}