package com.github.christophpickl.payconiq.rest.exceptions

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
