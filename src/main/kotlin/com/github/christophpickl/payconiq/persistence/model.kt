package com.github.christophpickl.payconiq.persistence

import java.time.LocalDateTime

data class StockDbo(
    val id: Long,
    val name: String,
    val currentPrice: AmountDbo,
    val lastUpdate: LocalDateTime
) {
    companion object
}

data class AmountDbo(
    val value: Long,
    val precision: Int,
    val currency: String
) {
    companion object {
        fun euro(euro: Long) = AmountDbo(euro, 0, "EUR")
    }
    fun add(newValue: Int) = copy(value = value + newValue)
}
