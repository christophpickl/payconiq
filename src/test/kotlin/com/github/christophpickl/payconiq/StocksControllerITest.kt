package com.github.christophpickl.payconiq

import com.github.christophpickl.payconiq.persistence.StockDbo
import com.github.christophpickl.payconiq.persistence.StocksRepository
import com.github.christophpickl.payconiq.rest.CreateStockRequestDto
import com.github.christophpickl.payconiq.rest.StockDto
import com.github.christophpickl.payconiq.rest.UpdateStockRequestDto
import com.github.christophpickl.payconiq.service.Clock
import com.github.christophpickl.payconiq.service.toAmountDto
import com.github.christophpickl.payconiq.service.toStockDbo
import com.github.christophpickl.payconiq.service.toStockDto
import com.github.christophpickl.payconiq.testInfrastructure.IntegrationTest
import com.github.christophpickl.payconiq.testInfrastructure.Method
import com.github.christophpickl.payconiq.testInfrastructure.Method.GET
import com.github.christophpickl.payconiq.testInfrastructure.Method.PUT
import com.github.christophpickl.payconiq.testInfrastructure.TestRestService
import com.github.christophpickl.payconiq.testInfrastructure.add
import com.github.christophpickl.payconiq.testInfrastructure.hasStatusCode
import com.github.christophpickl.payconiq.testInfrastructure.isEqualToIgnoringGivenProps
import com.github.christophpickl.payconiq.testInfrastructure.testInstance
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@IntegrationTest
class StocksControllerITest @Autowired constructor(
    private val rest: TestRestService
) {

    @MockBean
    private lateinit var mockStocksRepo: StocksRepository
    @MockBean
    private lateinit var mockClock: Clock

    private val stocksPath = "/api/stocks"
    private val stockDbo = StockDbo.testInstance
    private val stockDbos = listOf(stockDbo)
    private val anyStockDbo = stockDbo
    private val createStockDto = CreateStockRequestDto.testInstance
    private val anyCreateStockDto = createStockDto
    private val anyUploadtStockDto = UpdateStockRequestDto.testInstance
    private val nonExistingStockId = 42L
    private val time1 = LocalDateTime.parse("2018-10-03T21:42:30")
    private val time2 = time1.plusDays(1)
    private val anyTime = time1

    // GET /stocks
    // =================================================================================================================

    @Test
    fun `When get all stocks Then return status code 200 OK`() {
        val response = rest.request(GET, stocksPath)

        assertThat(response).hasStatusCode(HttpStatus.OK)
    }

    @Test
    fun `Given some stocks exist When get all stocks Then return those stocks`() {
        whenever(mockStocksRepo.fetchStocks()).thenReturn(stockDbos)

        val returnedStocks = rest.requestFor<List<StockDto>>(GET, stocksPath)

        assertThat(returnedStocks).isEqualTo(stockDbos.map { it.toStockDto() })
    }

    // GET /stocks/$stockId
    // =================================================================================================================

    @Test
    fun `Given stock not existing When get non-existing stock Then return status code 404 NOT FOUND`() {
        whenever(mockStocksRepo.fetchStock(nonExistingStockId)).thenReturn(null)

        val response = rest.request(GET, "$stocksPath/$nonExistingStockId")

        assertThat(response).hasStatusCode(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `Given stock exists When get this stock Then return status code 200 OK`() {
        whenever(mockStocksRepo.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val response = rest.request(GET, "$stocksPath/${stockDbo.id}")

        assertThat(response).hasStatusCode(HttpStatus.OK)
    }

    @Test
    fun `Given stock exists When get this stock Then return that stock`() {
        whenever(mockStocksRepo.fetchStock(stockDbo.id)).thenReturn(stockDbo)

        val returnedStock = rest.requestFor<StockDto>(GET, "$stocksPath/${stockDbo.id}")

        assertThat(returnedStock).isEqualTo(stockDbo.toStockDto())
    }

    // POST /stocks
    // =================================================================================================================

    @Test
    fun `Given repo saves stock When create new stock Then return status code 200 OK`() {
        whenever(mockStocksRepo.saveStock(any())).thenReturn(anyStockDbo)

        val response = rest.request(
            method = Method.POST,
            path = stocksPath,
            body = anyCreateStockDto
        )

        assertThat(response).hasStatusCode(HttpStatus.OK)
    }

    @Test
    fun `Given repo saves stock When create new stock Then save in repository`() {
        val expectedSaveStockDbo = createStockDto.toStockDbo()
        whenever(mockStocksRepo.saveStock(expectedSaveStockDbo)).thenReturn(anyStockDbo)

        rest.requestFor<StockDto>(
            method = Method.POST,
            path = stocksPath,
            body = createStockDto
        )

        verify(mockStocksRepo).saveStock(expectedSaveStockDbo)
    }

    @Test
    fun `Given repo saves stock When create new stock Then return that stock`() {
        val expectedStockDbo = createStockDto.toStockDbo()
        val persistedStockDbo = expectedStockDbo.copy(id = 1)
        whenever(mockStocksRepo.saveStock(expectedStockDbo)).thenReturn(persistedStockDbo)

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
        whenever(mockStocksRepo.fetchStock(nonExistingStockId)).thenReturn(null)

        val response = rest.request(
            method = PUT,
            path = "$stocksPath/$nonExistingStockId",
            body = anyUploadtStockDto
        )

        assertThat(response).hasStatusCode(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `Given stock exists When update that stock Then return status code 200 OK`() {
        whenever(mockStocksRepo.fetchStock(stockDbo.id)).thenReturn(stockDbo)
        whenever(mockClock.now()).thenReturn(anyTime)

        val response = rest.request(
            method = PUT,
            path = "$stocksPath/${stockDbo.id}",
            body = anyUploadtStockDto
        )

        assertThat(response).hasStatusCode(HttpStatus.OK)
    }

    @Test
    fun `Given stock exists When update that stock Then update in repository and return it`() {
        val updatedPrice = stockDbo.currentPrice.add(100)
        whenever(mockStocksRepo.fetchStock(stockDbo.id)).thenReturn(stockDbo)
        whenever(mockClock.now()).thenReturn(anyTime)

        val returnedStock = rest.requestFor<StockDto>(
            method = PUT,
            path = "$stocksPath/${stockDbo.id}",
            body = UpdateStockRequestDto(currentPrice = updatedPrice.toAmountDto())
        )

        val stockDboUpdated = stockDbo.copy(currentPrice = updatedPrice)
        verify(mockStocksRepo).updateStock(stockDboUpdated)
        assertThat(returnedStock).isEqualToIgnoringGivenProps(stockDboUpdated.toStockDto(), StockDto::lastUpdate)
    }

    @Test
    fun `Given stock exists When update that stock Then change its update timestamp`() {
        whenever(mockStocksRepo.fetchStock(stockDbo.id)).thenReturn(stockDbo.copy(lastUpdate = time1))
        whenever(mockClock.now()).thenReturn(time2)

        val returnedStock = rest.requestFor<StockDto>(
            method = PUT,
            path = "$stocksPath/${stockDbo.id}",
            body = UpdateStockRequestDto.testInstance
        )

        assertThat(returnedStock.lastUpdate).isEqualTo(time2)
    }

}
