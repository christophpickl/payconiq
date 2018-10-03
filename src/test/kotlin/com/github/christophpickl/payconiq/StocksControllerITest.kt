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
    private val stockDbo = StockDbo.testInstance
    private val stockDbos = listOf(stockDbo)
    private val nonExistingStockId = 42

    // GET /stocks
    // =================================================================================================================

    @Test
    fun `When GET stocks Then return status code 200 OK`() {
        val response = rest.getForEntity<Any>(stocksPath)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `Given some stocks exist When GET stocks Then return those stocks`() {
        whenever(stocksRepository.fetchStocks()).thenReturn(stockDbos)

        val returnedStocks = rest.getForEntityAssertingOk<List<StockDto>>(stocksPath)

        assertThat(returnedStocks).isEqualTo(stockDbos.map { it.toStockDto() })
    }

    // GET /stocks/$stockId
    // =================================================================================================================

    @Test
    fun `When GET non-existing stock Then return status code 404 NOT FOUND`() {
        val response = rest.getForEntity<Any>("$stocksPath/$nonExistingStockId")

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `Given stock exists When GET this stock Then return status code 200 OK`() {
        whenever(stocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val response = rest.getForEntity<Any>("$stocksPath/${stockDbo.id}")

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `Given stock exists When GET this stock Then return that stocks`() {
        whenever(stocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val returnedStock = rest.getForEntityAssertingOk<StockDto>("$stocksPath/${stockDbo.id}")

        assertThat(returnedStock).isEqualTo(stockDbo.toStockDto())
    }

}
