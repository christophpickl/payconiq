package com.github.christophpickl.payconiq.rest

import com.github.christophpickl.payconiq.service.NotFoundException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
@RestController
class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    private val log = KotlinLogging.logger {}

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(exception: NotFoundException): ResponseEntity<ApiError> {
        log.debug(exception) { "Not found handled." }
        return ResponseEntity(ApiError("Not found"), HttpStatus.NOT_FOUND)
    }

}

data class ApiError(
        val message: String
)
