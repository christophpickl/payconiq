package com.github.christophpickl.payconiq.persistence

import org.springframework.stereotype.Repository

interface StocksRepository {
    fun fetchStocks(): List<StockDbo>
    fun fetchStock(stockId: Long): StockDbo?
}

@Repository
class InMemoryStocksRepository : StocksRepository {

    private val stocksById = mutableMapOf<Long, StockDbo>()

    override fun fetchStock(stockId: Long): StockDbo? =
        stocksById[stockId]

    override fun fetchStocks(): List<StockDbo> =
        stocksById.values.toList()

}
