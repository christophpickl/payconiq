package com.github.christophpickl.payconiq.rest

import java.time.LocalDateTime

data class StockDto(
    val id: Long,
    val name: String,
    val currentPrice: AmountDto,
    val lastUpdate: LocalDateTime
)

data class CreateStockRequestDto(
    val name: String,
    val currentPrice: AmountDto,
    val lastUpdate: LocalDateTime
) {
    companion object
}

data class UpdateStockRequestDto(
    val currentPrice: AmountDto
) {
    companion object
}

data class AmountDto(
    val value: Long,
    val precision: Int,
    val currency: String
) {
    companion object {
        fun euro(euro: Long) = AmountDto(euro, 0, "EUR")
    }
}
