package com.github.christophpickl.payconiq.service

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import mu.KotlinLogging.logger as klogger

class NotFoundException(message: String) : RuntimeException(message)

@ControllerAdvice
@RestController
class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    private val log = klogger {}

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(exception: NotFoundException): ResponseEntity<ApiError> {
        log.warn(exception) { "Not found handled." }
        return ResponseEntity(ApiError("Not found"), HttpStatus.NOT_FOUND)
    }
}

data class ApiError(
    val message: String
)
