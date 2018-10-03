package com.github.christophpickl.payconiq.rest

import com.github.christophpickl.payconiq.service.StocksService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/stocks"])
class StocksController(
    private val service: StocksService
) {

    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    fun getStocks(): List<StockDto> =
        service.fetchStocks()

    @GetMapping(value = ["/{stockId}"], produces = [APPLICATION_JSON_VALUE])
    fun getStocks(@PathVariable("stockId") stockId: Long): StockDto =
        service.fetchStock(stockId)

}
