package com.github.christophpickl.payconiq.testInfrastructure

import com.github.christophpickl.payconiq.persistence.AmountDbo
import com.github.christophpickl.payconiq.persistence.StockDbo
import com.github.christophpickl.payconiq.rest.AmountDto
import com.github.christophpickl.payconiq.rest.UpdateStockDto
import java.time.LocalDateTime

val StockDbo.Companion.testInstance
    get() = StockDbo(
        id = 42,
        name = "testName",
        currentPrice = AmountDbo.testInstance,
        lastUpdate = LocalDateTime.parse("2018-10-03T21:42:30")
    )

val AmountDbo.Companion.testInstance
    get() = AmountDbo.euro(100)

val UpdateStockDto.Companion.testInstance
    get() = UpdateStockDto(AmountDto.testInstance)

val AmountDto.Companion.testInstance
    get() = AmountDto.euro(200)
