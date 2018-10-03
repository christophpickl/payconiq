package com.github.christophpickl.payconiq

import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping(value = ["/api/stocks"])
class StocksController(
        private val service: StocksService
) {

    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    fun getStocks(): List<StockDto> = service.fetchStocks()

}

data class StockDto(
        val id: Long,
        val name: String,
        val currentPrice: AmountDto,
        val lastUpdate: LocalDateTime
)

data class AmountDto(
        val value: Long,
        val precision: Int,
        val currency: String
) {
    companion object {
        fun euro(euro: Long) = AmountDto(euro, 0, "EUR")
    }
}
