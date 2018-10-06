package com.github.christophpickl.payconiq.rest.exceptions

import com.github.christophpickl.payconiq.isDevEnabled
import com.github.christophpickl.payconiq.rest.exceptions.ErrorCodeDto.BAD_REQUEST
import com.github.christophpickl.payconiq.rest.exceptions.ErrorCodeDto.UNKNOWN_ERROR
import com.github.christophpickl.payconiq.service.GenericException
import com.github.christophpickl.payconiq.service.NotFoundException
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
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
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    fun handleNotFoundException(exception: NotFoundException, request: HttpServletRequest) =
        buildApiError(exception, request.servletPath, ErrorCodeDto.NOT_FOUND)

    override fun handleHttpMessageNotReadable(
        exception: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> =
        ResponseEntity(buildApiError(exception, request.requestPath, BAD_REQUEST, overrideMessage = exception.message), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(Exception::class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleGenericException(exception: Exception, request: HttpServletRequest) =
        buildApiError(exception, request.servletPath, UNKNOWN_ERROR)

    private fun buildApiError(
        exception: Exception,
        requestPath: String,
        errorCode: ErrorCodeDto,
        overrideMessage: String? = null
    ) =
        ApiErrorDto(
            message = when {
                overrideMessage != null -> overrideMessage
                exception is GenericException -> exception.publicMessage
                else -> "Unknown server error!"
            },
            errorCode = errorCode,
            path = requestPath,
            exception = if (environment.isDevEnabled) exception.toExceptionDto() else null
        ).also { apiError ->
            log.debug(exception) { "Returning: $apiError" }
        }

    private val WebRequest.requestPath
        get() = ((this as? ServletWebRequest)?.nativeRequest as? HttpServletRequest)?.servletPath ?: ""

    private fun Throwable.toExceptionDto(): ExceptionDto = ExceptionDto(
        type = javaClass.name,
        message = message ?: "",
        trace = stackTrace.map { "${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})" },
        cause = cause?.toExceptionDto()
    )

}
