package com.github.christophpickl.payconiq.persistence

import org.springframework.stereotype.Repository

interface StocksRepository {
    fun fetchStocks(): List<StockDbo>
}

@Repository
class InMemoryStocksRepository : StocksRepository {
    override fun fetchStocks(): List<StockDbo> =
        emptyList()
}
