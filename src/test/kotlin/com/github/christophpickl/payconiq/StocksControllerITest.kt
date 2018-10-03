package com.github.christophpickl.payconiq

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@IntegrationTest
class StocksControllerITest @Autowired constructor(
        private val rest: TestRestTemplate
) {

    @Test
    fun `When GET stocks Then return status code 200`() {
        val response = rest.getForEntity<Any>("/api/stocks")

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `When GET stocks Then return list of some stocks`() {
        val response = rest.getForEntity<List<StockDto>>("/api/stocks")

        assertThat(response.body).isNotNull
        assertThat(response.body).isNotEmpty
    }

}
