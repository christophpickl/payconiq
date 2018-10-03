package com.github.christophpickl.payconiq.service

import com.github.christophpickl.payconiq.persistence.StocksRepository
import com.github.christophpickl.payconiq.rest.CreateStockRequestDto
import com.github.christophpickl.payconiq.rest.StockDto
import com.github.christophpickl.payconiq.rest.UpdateStockRequestDto
import mu.KotlinLogging.logger
import org.springframework.stereotype.Service

@Service
class StocksService(
    private val repo: StocksRepository
) {

    private val log = logger {}

    fun fetchStocks(): List<StockDto> {
        log.debug { "fetchStocks()" }
        return repo.fetchStocks().map { it.toStockDto() }
    }

    fun fetchStock(stockId: Long): StockDto {
        log.debug { "fetchStock(stockId=$stockId)" }
        return repo.fetchStock(stockId)?.toStockDto() ?: throw NotFoundException("Stock not found by ID: $stockId")
    }

    fun createStock(stockRequest: CreateStockRequestDto): StockDto {
        log.debug { "createStock(stockRequest=$stockRequest)" }
        val savedStock = repo.saveStock(stockRequest.toStockDbo())
        return savedStock.toStockDto()
    }

    fun updateStock(stockId: Long, stockRequest: UpdateStockRequestDto): StockDto {
        log.debug { "fetchStock(stockId=$stockId, stockRequest=$stockRequest)" }
        val storedStock = fetchStock(stockId)
        val updatedStock = storedStock.copyBy(stockRequest)
        repo.updateStock(updatedStock.toStockDbo())
        return updatedStock
    }

}

private fun StockDto.copyBy(stockRequest: UpdateStockRequestDto) = copy(
    currentPrice = stockRequest.currentPrice
)
