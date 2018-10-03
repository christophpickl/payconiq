package com.github.christophpickl.payconiq

import org.springframework.stereotype.Service

@Service
class StocksService(
    private val repo: StocksRepository
) {

    private val log = log {}

    fun fetchStocks(): List<StockDto> {
        log.info { "fetchStocks()" }
        return repo.fetchStocks().map { it.toStockDto() }
    }

}

fun StockDbo.toStockDto() = StockDto(
    id = id,
    name = name,
    currentPrice = currentPrice.toAmountDto(),
    lastUpdate = lastUpdate
)

fun AmountDbo.toAmountDto() = AmountDto(
    value = value,
    precision = precision,
    currency = currency
)
