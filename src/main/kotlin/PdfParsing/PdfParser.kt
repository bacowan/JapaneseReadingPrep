package PdfParsing

import net.sourceforge.tess4j.ITessAPI
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode.PSM_SINGLE_BLOCK_VERT_TEXT
import net.sourceforge.tess4j.Tesseract
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.apache.pdfbox.text.PDFTextStripper
import java.awt.image.BufferedImage
import java.io.File

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
            extractTextFromPdf(it, progress)
        }
        return textPages.map { singleLine(it) }
    }

    private fun extractTextFromPdf(doc: PDDocument, progress: ProgressReporter = ProgressReporter{}): List<String> {
        progress.setAsRatio(0, doc.numberOfPages)
        val pdfStripper = PDFTextStripper()
        val pdfRenderer = PDFRenderer(doc)
        val ret = mutableListOf<String>()
        (0 until doc.numberOfPages).forEach { pageNumber ->
            pdfStripper.startPage = pageNumber
            pdfStripper.endPage = pageNumber
            var text = pdfStripper.getText(doc)
            if (text.trim().isEmpty()) {
                text = getTextFromImage(pdfRenderer.renderImage(pageNumber, 1.toFloat(), ImageType.GRAY))
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
        val tess = Tesseract()
        tess.setPageSegMode(PSM_SINGLE_BLOCK_VERT_TEXT)
        tess.setLanguage("jpn_vert")
        return tess.doOCR(image)
    }
}