package com.github.christophpickl.payconiq

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment

@SpringBootApplication
class PayconiqApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<PayconiqApp>(*args)
        }
    }
}

val Environment.isDevEnabled get() = activeProfiles.contains("dev")
