package com.github.christophpickl.payconiq

import com.github.christophpickl.payconiq.testInfrastructure.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@IntegrationTest
class DummyITest @Autowired constructor(
        private val rest: TestRestTemplate
) {

    @Test
    fun `When GET dummy Then return status code 200`() {
        val response = rest.getForEntity("/dummy", Any::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

}
