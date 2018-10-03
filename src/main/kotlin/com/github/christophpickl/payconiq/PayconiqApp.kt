package com.github.christophpickl.payconiq

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class PayconiqApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<PayconiqApp>(*args)
        }
    }
}
