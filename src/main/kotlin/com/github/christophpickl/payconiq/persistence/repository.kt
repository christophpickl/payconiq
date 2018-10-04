package com.github.christophpickl.payconiq.persistence

import com.github.christophpickl.payconiq.service.NotFoundException
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicLong

interface StocksRepository {
    fun fetchStocks(): List<StockDbo>
    fun fetchStock(stockId: Long): StockDbo?
    fun updateStock(updateStock: StockDbo)
    fun saveStock(saveStock: StockDbo): StockDbo
}

@Repository
class InMemoryStocksRepository : StocksRepository {

    /** Visible for testing. */
    val stocksById = mutableMapOf<Long, StockDbo>()
    private val idGenerator = AtomicLong(1)

    override fun fetchStock(stockId: Long) =
        stocksById[stockId]

    override fun fetchStocks() =
        stocksById.values.toList()

    override fun saveStock(saveStock: StockDbo) =
        saveStock.copy(id = idGenerator.getAndIncrement()).apply {
            stocksById[id] = this
        }

    override fun updateStock(updateStock: StockDbo) {
        fetchStock(updateStock.id) ?: throw NotFoundException("Stock not found by ID: ${updateStock.id}")
        stocksById[updateStock.id] = updateStock
    }

}
