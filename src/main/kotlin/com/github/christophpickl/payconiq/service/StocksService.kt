package com.github.christophpickl.payconiq.service

import com.github.christophpickl.payconiq.persistence.StocksRepository
import com.github.christophpickl.payconiq.rest.CreateStockRequestDto
import com.github.christophpickl.payconiq.rest.StockDto
import com.github.christophpickl.payconiq.rest.UpdateStockRequestDto
import org.springframework.stereotype.Service

@Service
class StocksService(
    private val repo: StocksRepository,
    private val clock: Clock
) {

    @Logged
    fun fetchStocks(): List<StockDto> =
        repo.fetchStocks().map { it.toStockDto() }

    @Logged
    fun fetchStock(stockId: Long): StockDto =
        repo.fetchStock(stockId)?.toStockDto() ?: throw NotFoundException("Stock not found by ID: $stockId")

    @Logged
    fun createStock(stockRequest: CreateStockRequestDto): StockDto =
        repo.saveStock(stockRequest.toStockDbo()).toStockDto()

    @Logged
    fun updateStock(stockId: Long, updateRequest: UpdateStockRequestDto): StockDto =
        fetchStock(stockId)
            .copyBy(updateRequest)
            .copy(lastUpdate = clock.now())
            .also { updatedStock ->
                repo.updateStock(updatedStock.toStockDbo())
            }

}

private fun StockDto.copyBy(stockRequest: UpdateStockRequestDto) = copy(
    currentPrice = stockRequest.currentPrice
)
