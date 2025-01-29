package io.toasting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ToastingApplication

fun main(args: Array<String>) {
    runApplication<ToastingApplication>(*args)
}
