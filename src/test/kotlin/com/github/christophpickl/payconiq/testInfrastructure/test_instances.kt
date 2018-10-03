package com.github.christophpickl.payconiq.testInfrastructure

import com.github.christophpickl.payconiq.AmountDbo
import com.github.christophpickl.payconiq.StockDbo
import java.time.LocalDateTime

val StockDbo.Companion.testInstance
    get() = StockDbo(
        id = 1,
        name = "name",
        currentPrice = AmountDbo.testInstance,
        lastUpdate = LocalDateTime.now()
    )

val AmountDbo.Companion.testInstance
    get() = AmountDbo.euro(100)
