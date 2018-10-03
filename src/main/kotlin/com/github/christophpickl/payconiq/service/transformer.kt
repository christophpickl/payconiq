package com.github.christophpickl.payconiq.service

import com.github.christophpickl.payconiq.persistence.AmountDbo
import com.github.christophpickl.payconiq.persistence.StockDbo
import com.github.christophpickl.payconiq.rest.AmountDto
import com.github.christophpickl.payconiq.rest.StockDto

fun StockDbo.toStockDto() = StockDto(
    id = id,
    name = name,
    currentPrice = currentPrice.toAmountDto(),
    lastUpdate = lastUpdate
)

fun StockDto.toStockDbo() = StockDbo(
    id = id,
    name = name,
    currentPrice = currentPrice.toAmountDbo(),
    lastUpdate = lastUpdate
)

fun AmountDbo.toAmountDto() = AmountDto(
    value = value,
    precision = precision,
    currency = currency
)


fun AmountDto.toAmountDbo() = AmountDbo(
    value = value,
    precision = precision,
    currency = currency
)
