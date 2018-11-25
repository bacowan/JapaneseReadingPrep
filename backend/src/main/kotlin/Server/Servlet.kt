package Server

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "JapaneseReadingPrepServlet", urlPatterns = ["parse"], loadOnStartup = 1)
class Servlet: HttpServlet() {
    private lateinit var message: String

    @Override
    override fun init() {
        message = "Hello World"
    }

    @Override
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        resp?.contentType = "text/html"
        resp?.writer?.println("40")
    }
}