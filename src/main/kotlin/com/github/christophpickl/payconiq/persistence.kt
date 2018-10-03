package com.github.christophpickl.payconiq

import org.springframework.stereotype.Repository
import java.time.LocalDateTime

interface StocksRepository {
    fun fetchStocks(): List<StockDbo>
}

@Repository
class InMemoryStocksRepository : StocksRepository {
    override fun fetchStocks(): List<StockDbo> =
        emptyList()
    // listOf(StockDbo(1, "name", AmountDbo.euro(100), LocalDateTime.now()))
}

data class StockDbo(
        val id: Long,
        val name: String,
        val currentPrice: AmountDbo,
        val lastUpdate: LocalDateTime
) {
    companion object
}

data class AmountDbo(
        val value: Long,
        val precision: Int,
        val currency: String
) {
    companion object {
        fun euro(euro: Long) = AmountDbo(euro, 0, "EUR")
    }
}
