package io.toasting

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication()
@EnableJpaAuditing
class ToastingApplication : CommandLineRunner {
    @Autowired
    lateinit var ac: ApplicationContext

    override fun run(vararg args: String?) {
        ac.beanDefinitionNames.forEach { println(it) }
    }
}

fun main(args: Array<String>) {
    runApplication<ToastingApplication>(*args) {
    }
}
