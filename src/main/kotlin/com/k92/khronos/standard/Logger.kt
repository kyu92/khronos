package com.k92.khronos.standard

import org.slf4j.LoggerFactory

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Logger {
    companion object{
        val <reified T> T.log: org.slf4j.Logger
            inline get() = LoggerFactory.getLogger(T::class.java)
    }
}
