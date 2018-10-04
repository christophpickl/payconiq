package com.github.christophpickl.payconiq.service

import mu.KotlinLogging.logger as klogger

abstract class PayconiqException(
    message: String,
    val publicMessage: String = message
) : RuntimeException(message)

class NotFoundException(
    message: String,
    publicMessage: String = message
) : PayconiqException(
    message = message,
    publicMessage = publicMessage
)
