package com.github.christophpickl.payconiq.service

import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface Clock {
    fun now(): LocalDateTime
}

@Service
class RealClock : Clock {
    override fun now() = LocalDateTime.now()
}
