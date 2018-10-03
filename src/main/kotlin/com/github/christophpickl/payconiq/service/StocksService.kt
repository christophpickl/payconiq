package com.github.christophpickl.payconiq.service

import com.github.christophpickl.payconiq.persistence.StocksRepository
import com.github.christophpickl.payconiq.rest.StockDto
import mu.KotlinLogging.logger
import org.springframework.stereotype.Service

@Service
class StocksService(
    private val repo: StocksRepository
) {

    private val log = logger {}

    fun fetchStocks(): List<StockDto> {
        log.info { "fetchStocks()" }
        return repo.fetchStocks().map { it.toStockDto() }
    }

    fun fetchStock(stockId: Long): StockDto =
         repo.fetchStock(stockId)?.toStockDto() ?: throw NotFoundException("Stock not found by ID: $stockId")


}
