package com.github.christophpickl.payconiq.service

import com.github.christophpickl.payconiq.log
import com.github.christophpickl.payconiq.persistence.StocksRepository
import com.github.christophpickl.payconiq.rest.StockDto
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
