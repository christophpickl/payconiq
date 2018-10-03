package com.github.christophpickl.payconiq.persistence

import com.github.christophpickl.payconiq.service.NotFoundException
import org.springframework.stereotype.Repository

interface StocksRepository {
    fun fetchStocks(): List<StockDbo>
    fun fetchStock(stockId: Long): StockDbo?
    fun updateStock(updateStock: StockDbo)
}

@Repository
class InMemoryStocksRepository : StocksRepository {
    private val stocksById = mutableMapOf<Long, StockDbo>()

    override fun fetchStock(stockId: Long): StockDbo? =
        stocksById[stockId]

    override fun fetchStocks(): List<StockDbo> =
        stocksById.values.toList()

    override fun updateStock(updateStock: StockDbo) {
        fetchStock(updateStock.id) ?: throw NotFoundException("Stock not found by ID: ${updateStock.id}")
        stocksById[updateStock.id] = updateStock
    }

}
