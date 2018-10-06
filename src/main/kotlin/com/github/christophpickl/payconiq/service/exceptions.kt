package com.github.christophpickl.payconiq.service

import mu.KotlinLogging.logger as klogger

abstract class GenericException(
    message: String,
    val publicMessage: String = message
) : RuntimeException(message)

class NotFoundException(
    message: String,
    publicMessage: String = message
) : GenericException(
    message = message,
    publicMessage = publicMessage
)
