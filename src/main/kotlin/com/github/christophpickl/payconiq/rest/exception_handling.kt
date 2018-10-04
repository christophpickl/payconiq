package com.github.christophpickl.payconiq.rest

import com.github.christophpickl.payconiq.isDevEnabled
import com.github.christophpickl.payconiq.service.NotFoundException
import com.github.christophpickl.payconiq.service.PayconiqException
import org.springframework.core.env.Environment
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
class CustomExceptionHandler(
    private val environment: Environment
) : ResponseEntityExceptionHandler() {

    private val log = mu.KotlinLogging.logger {}

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleNotFoundException(exception: NotFoundException, request: HttpServletRequest) =
        buildApiError(exception, request, ErrorCodeDto.NOT_FOUND)

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleException(exception: Exception, request: HttpServletRequest) =
        buildApiError(exception, request, ErrorCodeDto.UNKNOWN_ERROR)

    private fun buildApiError(exception: Exception, request: HttpServletRequest, errorCode: ErrorCodeDto): ApiErrorDto {
        val message = if (exception is PayconiqException) {
            exception.publicMessage
        } else {
            "Unknown internal server error!"
        }
        return ApiErrorDto(
            message = message,
            errorCode = errorCode,
            path = request.servletPath,
            exception = if (environment.isDevEnabled) exception.toExceptionDto() else null
        ).also { apiError ->
            log.debug(exception) { "Returning: $apiError" }
        }
    }
}

data class ApiErrorDto(
    val message: String,
    val errorCode: ErrorCodeDto,
    val path: String,
    val exception: ExceptionDto? = null
)

enum class ErrorCodeDto {
    UNKNOWN_ERROR,
    NOT_FOUND
}

data class ExceptionDto(
    val type: String,
    val message: String,
    val trace: List<String>,
    val cause: ExceptionDto?
)

private fun Throwable.toExceptionDto(): ExceptionDto {
    return ExceptionDto(
        type = javaClass.name,
        message = message ?: "",
        trace = stackTrace.map { "${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})" },
        cause = cause?.toExceptionDto()
    )
}
