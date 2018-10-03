package com.github.christophpickl.payconiq

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@IntegrationTest
class StocksControllerITest @Autowired constructor(
        private val rest: TestRestTemplate
) {

    @MockBean
    private lateinit var stocksRepository: StocksRepository

    @Test
    fun `When GET stocks Then return status code 200`() {
        val response = rest.getForEntity<Any>("/api/stocks")

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

//    @Test
//    fun `Given repo returns some stocks When GET stocks Then return those stocks`() {
//        val givenStocks = listOf(StockDbo.testInstance)
//        whenever(stocksRepository.fetchStocks()).thenReturn(givenStocks)
//
//        val response2 = rest.getForEntity<String>("/api/stocks")
//        println(jacksonObjectMapper().readValue<List<StockDto>>(response2.body!!))
//        println(response2.body)
//
//        val response = rest.getForEntity<List<StockDto>>("/api/stocks")
//
//        assertThat(response.body).isNotNull
//        assertThat(response.body).isEqualTo(givenStocks)
//    }

}

val StockDbo.Companion.testInstance
    get() = StockDbo(
            id = 1,
            name = "name",
            currentPrice = AmountDbo.testInstance,
            lastUpdate = LocalDateTime.now()
    )

val AmountDbo.Companion.testInstance get() = AmountDbo.euro(100)
