import org.junit.Assert
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import org.junit.Test

class MainTest {
    @Test
    fun name() {
        val main = Main()
        val out = ByteArrayOutputStream()
        main.print(PrintStream(out))
        Assert.assertEquals("Hello world!", out.toString())
    }
}