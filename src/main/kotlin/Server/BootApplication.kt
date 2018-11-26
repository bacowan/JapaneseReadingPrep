package Server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class BootApplication

fun main(args: Array<String>) {
    runApplication<BootApplication>(*args)
}
