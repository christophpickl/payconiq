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
import com.github.christophpickl.payconiq.testInfrastructure.hasStatusCode
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
    private val anyStockDbo = stockDbo
    private val createStockDto = CreateStockRequestDto.testInstance
    private val nonExistingStockId = 42L

    // GET /stocks
    // =================================================================================================================

    @Test
    fun `When get all stocks Then return status code 200 OK`() {
        val response = rest.request(GET, stocksPath)

        assertThat(response).hasStatusCode(HttpStatus.OK)
    }

    @Test
    fun `Given some stocks exist When get all stocks Then return those stocks`() {
        whenever(mockStocksRepository.fetchStocks()).thenReturn(stockDbos)

        val returnedStocks = rest.requestFor<List<StockDto>>(GET, stocksPath)

        assertThat(returnedStocks).isEqualTo(stockDbos.map { it.toStockDto() })
    }

    // GET /stocks/$stockId
    // =================================================================================================================

    @Test
    fun `Given stock not existing When get non-existing stock Then return status code 404 NOT FOUND`() {
        whenever(mockStocksRepository.fetchStock(nonExistingStockId)).thenReturn(null)

        val response = rest.request(GET, "$stocksPath/$nonExistingStockId")

        assertThat(response).hasStatusCode(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `Given stock exists When get this stock Then return status code 200 OK`() {
        whenever(mockStocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val response = rest.request(GET, "$stocksPath/${stockDbo.id}")

        assertThat(response).hasStatusCode(HttpStatus.OK)
    }

    @Test
    fun `Given stock exists When get this stock Then return that stocks`() {
        whenever(mockStocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val returnedStock = rest.requestFor<StockDto>(GET, "$stocksPath/${stockDbo.id}")

        assertThat(returnedStock).isEqualTo(stockDbo.toStockDto())
    }

    // POST /stocks
    // =================================================================================================================

    @Test
    fun `Given repo saves stock When create new stock Then return status code 200 OK`() {
        whenever(mockStocksRepository.saveStock(any())).thenReturn(StockDbo.testInstance)

        val response = rest.request(
            method = Method.POST,
            path = stocksPath,
            body = CreateStockRequestDto.testInstance
        )

        assertThat(response).hasStatusCode(HttpStatus.OK)
    }

    @Test
    fun `Given repo saves stock When create new stock Then save in repository`() {
        val expectedStockDbo = createStockDto.toStockDbo()
        whenever(mockStocksRepository.saveStock(expectedStockDbo)).thenReturn(anyStockDbo)

        rest.requestFor<StockDto>(
            method = Method.POST,
            path = stocksPath,
            body = createStockDto
        )

        verify(mockStocksRepository).saveStock(expectedStockDbo)
    }

    @Test
    fun `Given repo saves stock When create new stock Then return it`() {
        val expectedStockDbo = createStockDto.toStockDbo()
        val persistedStockDbo = expectedStockDbo.copy(id = 1)
        whenever(mockStocksRepository.saveStock(expectedStockDbo)).thenReturn(persistedStockDbo)

        val returnedStock = rest.requestFor<StockDto>(
            method = Method.POST,
            path = stocksPath,
            body = createStockDto
        )

        assertThat(returnedStock).isEqualTo(persistedStockDbo.toStockDto())
    }

    // PUT /stocks/$stockId
    // =================================================================================================================

    @Test
    fun `Given stock not existing When PUT non-existing stock Then return status code 404 NOT FOUND`() {
        whenever(mockStocksRepository.fetchStock(nonExistingStockId)).thenReturn(null)

        val response = rest.request(
            method = PUT,
            path = "$stocksPath/$nonExistingStockId",
            body = UpdateStockRequestDto.testInstance
        )

        assertThat(response).hasStatusCode(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `Given stock exists When update that stock Then return status code 200 OK`() {
        whenever(mockStocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val response = rest.request(
            method = PUT,
            path = "$stocksPath/${stockDbo.id}",
            body = UpdateStockRequestDto.testInstance
        )

        assertThat(response).hasStatusCode(HttpStatus.OK)
    }

    @Test
    fun `Given stock exists When update that stock Then update in repository and return it`() {
        val updatedPrice = stockDbo.currentPrice.add(100)
        whenever(mockStocksRepository.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val returnedStock = rest.requestFor<StockDto>(
            method = PUT,
            path = "$stocksPath/${stockDbo.id}",
            body = UpdateStockRequestDto(currentPrice = updatedPrice.toAmountDto())
        )

        val stockDboUpdated = stockDbo.copy(currentPrice = updatedPrice)
        verify(mockStocksRepository).updateStock(stockDboUpdated)
        assertThat(returnedStock).isEqualTo(stockDboUpdated.toStockDto())
    }

}
