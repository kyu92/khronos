package com.k92.khronos

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class KhronosApplication

fun main(args: Array<String>) {
    runApplication<KhronosApplication>(*args)
}
