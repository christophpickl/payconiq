package com.github.christophpickl.payconiq

import com.github.christophpickl.payconiq.persistence.StockDbo
import com.github.christophpickl.payconiq.persistence.StocksRepository
import com.github.christophpickl.payconiq.rest.StockDto
import com.github.christophpickl.payconiq.rest.UpdateStockRequestDto
import com.github.christophpickl.payconiq.service.toAmountDto
import com.github.christophpickl.payconiq.service.toStockDto
import com.github.christophpickl.payconiq.testInfrastructure.IntegrationTest
import com.github.christophpickl.payconiq.testInfrastructure.Method.GET
import com.github.christophpickl.payconiq.testInfrastructure.Method.PUT
import com.github.christophpickl.payconiq.testInfrastructure.TestRestService
import com.github.christophpickl.payconiq.testInfrastructure.testInstance
import com.nhaarman.mockito_kotlin.verify
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
    fun `When get all stocks Then return status code 200 OK`() {
        val response = rest.execute(GET, stocksPath)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `Given some stocks exist When get all stocks Then return those stocks`() {
        whenever(stocksRepository.fetchStocks()).thenReturn(stockDbos)

        val returnedStocks = rest.executeExpectingStatusCode<List<StockDto>>(GET, stocksPath)

        assertThat(returnedStocks).isEqualTo(stockDbos.map { it.toStockDto() })
    }

    // GET /stocks/$stockId
    // =================================================================================================================

    @Test
    fun `When get non-existing stock Then return status code 404 NOT FOUND`() {
        val response = rest.execute(GET, "$stocksPath/$nonExistingStockId")

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `Given stock exists When get this stock Then return status code 200 OK`() {
        whenever(stocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val response = rest.execute(GET, "$stocksPath/${stockDbo.id}")

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `Given stock exists When get this stock Then return that stocks`() {
        whenever(stocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val returnedStock = rest.executeExpectingStatusCode<StockDto>(GET, "$stocksPath/${stockDbo.id}")

        assertThat(returnedStock).isEqualTo(stockDbo.toStockDto())
    }

    // PUT /stocks/$stockId
    // =================================================================================================================

    @Test
    fun `When PUT non-existing stock Then return status code 404 NOT FOUND`() {
        val response = rest.execute(
            method = PUT,
            path = "$stocksPath/$nonExistingStockId",
            body = UpdateStockRequestDto.testInstance
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `Given stock exists When update that stock Then return status code 200 OK`() {
        whenever(stocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val response = rest.execute(
            method = PUT,
            path = "$stocksPath/${stockDbo.id}",
            body = UpdateStockRequestDto.testInstance
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `Given stock exists When update that stock Then update in repository and return it`() {
        val updatedPrice = stockDbo.currentPrice.add(100)
        whenever(stocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val returnedStock = rest.executeExpectingStatusCode<StockDto>(
            method = PUT,
            path = "$stocksPath/${stockDbo.id}",
            body = UpdateStockRequestDto(currentPrice = updatedPrice.toAmountDto())
        )

        val stockDboUpdated = stockDbo.copy(currentPrice = updatedPrice)
        verify(stocksRepository).updateStock(stockDboUpdated)
        assertThat(returnedStock).isEqualTo(stockDboUpdated.toStockDto())
    }

}
