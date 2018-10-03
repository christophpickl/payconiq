package com.github.christophpickl.payconiq

import com.github.christophpickl.payconiq.persistence.StockDbo
import com.github.christophpickl.payconiq.persistence.StocksRepository
import com.github.christophpickl.payconiq.rest.CreateStockRequestDto
import com.github.christophpickl.payconiq.rest.StockDto
import com.github.christophpickl.payconiq.rest.UpdateStockRequestDto
import com.github.christophpickl.payconiq.service.toAmountDto
import com.github.christophpickl.payconiq.service.toStockDbo
import com.github.christophpickl.payconiq.service.toStockDto
import com.github.christophpickl.payconiq.testInfrastructure.IntegrationTest
import com.github.christophpickl.payconiq.testInfrastructure.Method
import com.github.christophpickl.payconiq.testInfrastructure.Method.GET
import com.github.christophpickl.payconiq.testInfrastructure.Method.PUT
import com.github.christophpickl.payconiq.testInfrastructure.TestRestService
import com.github.christophpickl.payconiq.testInfrastructure.testInstance
import com.nhaarman.mockito_kotlin.any
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
    private lateinit var mockStocksRepository: StocksRepository
    private val stocksPath = "/api/stocks"
    private val stockDbo = StockDbo.testInstance
    private val stockDbos = listOf(stockDbo)
    private val createStockDto = CreateStockRequestDto.testInstance
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
        whenever(mockStocksRepository.fetchStocks()).thenReturn(stockDbos)

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
        whenever(mockStocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val response = rest.execute(GET, "$stocksPath/${stockDbo.id}")

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `Given stock exists When get this stock Then return that stocks`() {
        whenever(mockStocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val returnedStock = rest.executeExpectingStatusCode<StockDto>(GET, "$stocksPath/${stockDbo.id}")

        assertThat(returnedStock).isEqualTo(stockDbo.toStockDto())
    }

    // POST /stocks
    // =================================================================================================================

    @Test
    fun `Given repo saves stock When create new stock Then return status code 200 OK`() {
        whenever(mockStocksRepository.saveStock(any())).thenReturn(StockDbo.testInstance)

        val response = rest.execute(
            method = Method.POST,
            path = stocksPath,
            body = CreateStockRequestDto.testInstance
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `Given repo saves stock When create new stock Then return save in repository and return it`() {
        val expectedStockDto = createStockDto.toStockDto()
        val persistedStockDto = expectedStockDto.copy(id = 1)
        whenever(mockStocksRepository.saveStock(expectedStockDto.toStockDbo())).thenReturn(persistedStockDto.toStockDbo())

        val returnedStock = rest.executeExpectingStatusCode<StockDto>(
            method = Method.POST,
            path = stocksPath,
            body = createStockDto
        )

        verify(mockStocksRepository).saveStock(expectedStockDto.toStockDbo())
        assertThat(returnedStock).isEqualTo(persistedStockDto)
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
        whenever(mockStocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

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
        whenever(mockStocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val returnedStock = rest.executeExpectingStatusCode<StockDto>(
            method = PUT,
            path = "$stocksPath/${stockDbo.id}",
            body = UpdateStockRequestDto(currentPrice = updatedPrice.toAmountDto())
        )

        val stockDboUpdated = stockDbo.copy(currentPrice = updatedPrice)
        verify(mockStocksRepository).updateStock(stockDboUpdated)
        assertThat(returnedStock).isEqualTo(stockDboUpdated.toStockDto())
    }

}
