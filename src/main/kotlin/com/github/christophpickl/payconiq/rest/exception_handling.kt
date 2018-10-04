package com.github.christophpickl.payconiq.rest

import com.github.christophpickl.payconiq.service.NotFoundException
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

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleNotFoundException(exception: NotFoundException, request: HttpServletRequest) = ApiError(
        message = exception.publicMessage,
        errorCode = ErrorCode.NOT_FOUND,
        path = request.servletPath
    )

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleGenericException(exception: Exception, request: HttpServletRequest) = ApiError(
        message = "Unknown internal server error",
        errorCode = ErrorCode.UNKNOWN_ERROR,
        path = request.servletPath
    )

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
