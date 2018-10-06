package com.github.christophpickl.payconiq

import com.github.christophpickl.payconiq.rest.exceptions.ApiErrorDto
import com.github.christophpickl.payconiq.rest.exceptions.ErrorCodeDto
import com.github.christophpickl.payconiq.rest.exceptions.ErrorCodeDto.BAD_REQUEST
import com.github.christophpickl.payconiq.service.NotFoundException
import com.github.christophpickl.payconiq.testInfrastructure.IntegrationTest
import com.github.christophpickl.payconiq.testInfrastructure.Method.GET
import com.github.christophpickl.payconiq.testInfrastructure.Method.POST
import com.github.christophpickl.payconiq.testInfrastructure.TestRestService
import com.github.christophpickl.payconiq.testInfrastructure.isEqualToIgnoringGivenProps
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private const val BASE_PATH = "/test/exceptions"

private const val SUB_NULLPOINTER_PATH = "/nullpointer"
private const val NULLPOINTER_PATH = "$BASE_PATH$SUB_NULLPOINTER_PATH"

private const val SUB_NOTFOUND_PATH = "/notfound"
private const val NOTFOUND_PATH = "$BASE_PATH$SUB_NOTFOUND_PATH"
private const val NOTFOUND_MESSAGE = "test not found"

private const val SUB_REQUESTBODY_PATH = "/requestbody"
private const val REQUESTBODY_PATH = "$BASE_PATH$SUB_REQUESTBODY_PATH"

@IntegrationTest
class CustomExceptionHandlerITest @Autowired constructor(
    private val rest: TestRestService
) {

    @Test
    fun `When throw NullPointerException Then return status code 500 and proper ApiError`() {
        assertRequest(
            path = NULLPOINTER_PATH,
            expectedStatusCode = HttpStatus.INTERNAL_SERVER_ERROR,
            expectedErrorCode = ErrorCodeDto.UNKNOWN_ERROR
        )
    }

    @Test
    fun `When throw NotFoundException Then return status code 404 and proper ApiError`() {
        assertRequest(
            path = NOTFOUND_PATH,
            expectedStatusCode = HttpStatus.NOT_FOUND,
            expectedMessage = NOTFOUND_MESSAGE,
            expectedErrorCode = ErrorCodeDto.NOT_FOUND
        )
    }

    @Test
    fun `When send invalid request body Then return status code 400 and proper ApiError containing missing field name`() {
        val apiError = rest.requestFor<ApiErrorDto>(
            method = POST,
            path = REQUESTBODY_PATH,
            body = InvalidRequestBodyDto(),
            expectedStatusCode = HttpStatus.BAD_REQUEST
        )

        assertThat(apiError).isEqualToIgnoringGivenProps(ApiErrorDto(
            message = "",
            errorCode = BAD_REQUEST,
            path = REQUESTBODY_PATH
        ), ApiErrorDto::message)
        assertThat(apiError.message).contains(RequestBodyDto::missingField.name)
    }

    private fun assertRequest(
        path: String,
        expectedStatusCode: HttpStatus,
        expectedMessage: String? = null,
        expectedErrorCode: ErrorCodeDto
    ) {
        val apiError = rest.requestFor<ApiErrorDto>(
            method = GET,
            path = path,
            expectedStatusCode = expectedStatusCode)

        assertThat(apiError).isEqualTo(ApiErrorDto(
            message = expectedMessage ?: apiError.message,
            errorCode = expectedErrorCode,
            path = path
        ))
    }

}

@RestController
@RequestMapping(value = [BASE_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class TestExceptionsController {

    @GetMapping(SUB_NULLPOINTER_PATH)
    fun throwNullPointerException(): Any {
        throw NullPointerException()
    }

    @GetMapping(SUB_NOTFOUND_PATH)
    fun throwNotFoundException(): Any {
        throw NotFoundException(
            publicMessage = NOTFOUND_MESSAGE,
            message = "ignore"
        )
    }

    @PostMapping(SUB_REQUESTBODY_PATH, consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun withRequestBody(
        @RequestBody body: RequestBodyDto
    ) = ResponseEntity.ok()

}

data class RequestBodyDto(
    val someField: Int,
    val missingField: String
)

data class InvalidRequestBodyDto(
    val someField: Int = 42
)
