package com.github.christophpickl.payconiq.rest

import com.github.christophpickl.payconiq.isDevEnabled
import com.github.christophpickl.payconiq.service.NotFoundException
import com.github.christophpickl.payconiq.service.PayconiqException
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
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
        buildApiError(exception, request.servletPath, ErrorCodeDto.NOT_FOUND)

    override fun handleHttpMessageNotReadable(
        exception: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> =
        ResponseEntity(buildApiError(exception, request.requestPath, ErrorCodeDto.BAD_REQUEST, overrideMessage = exception.message), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleGenericException(exception: Exception, request: HttpServletRequest) =
        buildApiError(exception, request.servletPath, ErrorCodeDto.UNKNOWN_ERROR)

    private fun buildApiError(
        exception: Exception,
        requestPath: String,
        errorCode: ErrorCodeDto,
        overrideMessage: String? = null
    ) =
        ApiErrorDto(
            message = when {
                overrideMessage != null -> overrideMessage
                exception is PayconiqException -> exception.publicMessage
                else -> "Unknown server error!"
            },
            errorCode = errorCode,
            path = requestPath,
            exception = if (environment.isDevEnabled) exception.toExceptionDto() else null
        ).also { apiError ->
            log.debug(exception) { "Returning: $apiError" }
        }

    private val WebRequest.requestPath
        get() =
            ((this as? ServletWebRequest)?.nativeRequest as? HttpServletRequest)?.servletPath ?: ""

}

data class ApiErrorDto(
    val message: String,
    val errorCode: ErrorCodeDto,
    val path: String,
    val exception: ExceptionDto? = null
)

enum class ErrorCodeDto {
    UNKNOWN_ERROR,
    BAD_REQUEST,
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
