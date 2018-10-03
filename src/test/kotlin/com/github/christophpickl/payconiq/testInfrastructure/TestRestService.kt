package com.github.christophpickl.payconiq.testInfrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class TestRestService(
    val internalRest: TestRestTemplate,
    val mapper: ObjectMapper
) {

    inline fun <reified T : Any> getForEntity(url: String) = internalRest.getForEntity<T>("/api/stocks")

    inline fun <reified T : Any> getForEntityAssertingOk(url: String): T {
        val response = internalRest.getForEntity<String>(url)
        assertThat(response.statusCode).describedAs("Response was: $response").isEqualTo(HttpStatus.OK)
        return mapper.readValue<T>(response.body!!)
    }

}
