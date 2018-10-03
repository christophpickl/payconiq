package com.github.christophpickl.payconiq

import com.github.christophpickl.payconiq.persistence.StockDbo
import com.github.christophpickl.payconiq.persistence.StocksRepository
import com.github.christophpickl.payconiq.rest.StockDto
import com.github.christophpickl.payconiq.service.toStockDto
import com.github.christophpickl.payconiq.testInfrastructure.IntegrationTest
import com.github.christophpickl.payconiq.testInfrastructure.TestRestService
import com.github.christophpickl.payconiq.testInfrastructure.testInstance
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus

@IntegrationTest
class StocksControllerITest @Autowired constructor(
        private val rest: TestRestService
) {

    @MockBean
    private lateinit var stocksRepository: StocksRepository

    private val stocksPath = "/api/stocks"
    private val stockDbos = listOf(StockDbo.testInstance)

    @Test
    fun `When GET stocks Then return status code 200`() {
        val response = rest.getForEntity<Any>(stocksPath)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `Given repo returns some stocks When GET stocks Then return those stocks`() {
        whenever(stocksRepository.fetchStocks()).thenReturn(stockDbos)

        val stocks = rest.getForEntityAssertingOk<List<StockDto>>(stocksPath)

        assertThat(stocks).isEqualTo(stockDbos.map { it.toStockDto() })
    }

}
