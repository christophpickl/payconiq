package com.github.christophpickl.payconiq.persistence

import com.github.christophpickl.payconiq.service.NotFoundException
import com.github.christophpickl.payconiq.testInfrastructure.testInstance
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InMemoryStocksRepositoryTest {

    private lateinit var testee: InMemoryStocksRepository
    private val stockDbo = StockDbo.testInstance
    private val stockDboChanged = StockDbo(
        id = stockDbo.id,
        name = stockDbo.name + "2",
        currentPrice = stockDbo.currentPrice.add(1),
        lastUpdate = stockDbo.lastUpdate.plusYears(1)
    )
    private val anyId = 42L

    @BeforeEach
    fun `init testee`() {
        testee = InMemoryStocksRepository()
    }

    @Test
    fun `Given single stock exists When fetch stocks Then return list containing that stock`() {
        testee.stocksById[1] = stockDbo

        val stocks = testee.fetchStocks()

        assertThat(stocks).isEqualTo(listOf(stockDbo))
    }

    @Test
    fun `When fetch non-existing stock Then return null`() {
        val stock = testee.fetchStock(anyId)

        assertThat(stock).isNull()
    }

    @Test
    fun `Given single stock exists When fetch that stock Then return it`() {
        testee.stocksById[stockDbo.id] = stockDbo

        val stock = testee.fetchStock(stockDbo.id)

        assertThat(stock).isEqualTo(stockDbo)
    }

    @Test
    fun `When update non-existing stock Then throw`() {
        assertThatThrownBy {
            testee.updateStock(stockDbo)
        }.isExactlyInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `Given single stock exists When update that stock Then change it`() {
        testee.stocksById[stockDbo.id] = stockDbo

        testee.updateStock(stockDboChanged)

        assertThat(testee.stocksById[stockDbo.id]).isEqualTo(stockDboChanged)
    }

    @Test
    fun `When create stock Then add and return it`() {
        val savedStock = testee.saveStock(stockDbo)

        assertThat(testee.stocksById[savedStock.id]).isEqualToIgnoringGivenFields(stockDbo, "id")
        assertThat(savedStock).isEqualToIgnoringGivenFields(stockDbo, "id")
    }

    @Test
    fun `When create two stocks Then ID is different`() {
        val savedStock1 = testee.saveStock(stockDbo)
        val savedStock2 = testee.saveStock(stockDbo)

        assertThat(savedStock1.id).isNotEqualTo(savedStock2.id)
    }

}
