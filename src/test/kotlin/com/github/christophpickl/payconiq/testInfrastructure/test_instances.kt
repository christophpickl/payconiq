package com.github.christophpickl.payconiq.testInfrastructure

import com.github.christophpickl.payconiq.persistence.AmountDbo
import com.github.christophpickl.payconiq.persistence.StockDbo
import com.github.christophpickl.payconiq.rest.AmountDto
import com.github.christophpickl.payconiq.rest.CreateStockRequestDto
import com.github.christophpickl.payconiq.rest.UpdateStockRequestDto
import java.time.LocalDateTime

val StockDbo.Companion.testInstance
    get() = StockDbo(
        id = 42,
        name = "testName",
        currentPrice = AmountDbo.testInstance,
        lastUpdate = LocalDateTime.parse("2018-10-03T21:42:30")
    )

val StockDbo.Companion.testInstanceChanged
    get() = StockDbo(
        id = StockDbo.testInstance.id,
        name = StockDbo.testInstance.name + "x",
        currentPrice = StockDbo.testInstance.currentPrice.add(1),
        lastUpdate = StockDbo.testInstance.lastUpdate.plusYears(1)
    )

val UpdateStockRequestDto.Companion.testInstance
    get() = UpdateStockRequestDto(AmountDto.testInstance)

val CreateStockRequestDto.Companion.testInstance
    get() = CreateStockRequestDto(
        name = "createName",
        currentPrice = AmountDto.testInstance,
        lastUpdate = LocalDateTime.parse("2019-10-03T21:42:30")
    )

val AmountDto.Companion.testInstance
    get() = AmountDto.euro(200)

val AmountDbo.Companion.testInstance
    get() = AmountDbo.euro(100)

fun AmountDbo.add(addValue: Int) = copy(value = value + addValue)
