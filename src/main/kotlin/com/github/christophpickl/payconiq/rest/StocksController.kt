package com.github.christophpickl.payconiq.rest

import com.github.christophpickl.payconiq.service.Logged
import com.github.christophpickl.payconiq.service.StocksService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/stocks"], produces = [APPLICATION_JSON_VALUE])
class StocksController(
    private val service: StocksService
) {

    @GetMapping
    @Logged
    fun getStocks(): List<StockDto> =
        service.fetchStocks()

    @GetMapping(value = ["/{stockId}"])
    @Logged
    fun getStocks(
        @PathVariable("stockId") stockId: Long
    ): StockDto =
        service.fetchStock(stockId)

    @PostMapping(consumes = [APPLICATION_JSON_VALUE])
    @Logged
    fun createStock(
        @RequestBody stockRequest: CreateStockRequestDto
    ): StockDto =
        service.createStock(stockRequest)

    @PutMapping(value = ["/{stockId}"], consumes = [APPLICATION_JSON_VALUE])
    @Logged
    fun updateStock(
        @PathVariable("stockId") stockId: Long,
        @RequestBody stockRequest: UpdateStockRequestDto
    ): StockDto =
        service.updateStock(stockId, stockRequest)

}
