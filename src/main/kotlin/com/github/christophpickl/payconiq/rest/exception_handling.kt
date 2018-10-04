package com.github.christophpickl.payconiq.rest

import com.github.christophpickl.payconiq.service.NotFoundException
import com.github.christophpickl.payconiq.service.PayconiqException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
@RestController
class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    private val log = mu.KotlinLogging.logger {}

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleNotFoundException(exception: NotFoundException, request: HttpServletRequest) =
        buildApiError(exception, request, ErrorCode.NOT_FOUND)

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleException(exception: Exception, request: HttpServletRequest) =
        buildApiError(exception, request, ErrorCode.UNKNOWN_ERROR)

    private fun buildApiError(exception: Exception, request: HttpServletRequest, errorCode: ErrorCode): ApiError {
        val message = if (exception is PayconiqException) {
            exception.publicMessage
        } else {
            "Unknown internal server error!"
        }
        return ApiError(
            message = message,
            errorCode = errorCode,
            path = request.servletPath
        ).also { apiError ->
            log.debug(exception) { "Returning: $apiError" }
        }
    }
}

data class ApiError(
    val message: String,
    val errorCode: ErrorCode,
    val path: String
)

enum class ErrorCode {
    UNKNOWN_ERROR,
    NOT_FOUND
}
