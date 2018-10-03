package com.github.christophpickl.payconiq

import org.springframework.stereotype.Service

@Service
class StocksService(
        private val repo: StocksRepository
) {

    fun fetchStocks(): List<StockDto> =
            repo.fetchStocks().map { it.toStockDto() }

    private fun StockDbo.toStockDto() = StockDto(
            id = id,
            name = name,
            currentPrice = currentPrice.toAmountDto(),
            lastUpdate = lastUpdate
    )

    private fun AmountDbo.toAmountDto() = AmountDto(
            value = value,
            precision = precision,
            currency = currency
    )

}
