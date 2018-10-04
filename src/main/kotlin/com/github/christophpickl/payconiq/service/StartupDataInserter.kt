package com.github.christophpickl.payconiq.service

import com.github.christophpickl.payconiq.isTestEnabled
import com.github.christophpickl.payconiq.persistence.AmountDbo
import com.github.christophpickl.payconiq.persistence.StockDbo
import com.github.christophpickl.payconiq.persistence.StocksRepository
import mu.KotlinLogging.logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class StartupDataInserter(
    private val stocksRepository: StocksRepository,
    private val environment: Environment
) {

    private val log = logger {}

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReadyEvent() {
        if (environment.isTestEnabled) {
            return
        }

        log.info { "Inserting startup stock items." }
        listOf(
            StockDbo(0, "NASDAQ:AAPL", AmountDbo.euro(868), LocalDateTime.now()),
            StockDbo(0, "NASDAQ:GOOGL", AmountDbo.euro(720), LocalDateTime.now()),
            StockDbo(0, "NASDAQ:MSFT", AmountDbo.euro(600), LocalDateTime.now()),
            StockDbo(0, "NASDAQ:AMZN", AmountDbo.euro(560), LocalDateTime.now()),
            StockDbo(0, "NASDAQ:FB", AmountDbo.euro(508), LocalDateTime.now())
        ).forEach {
            stocksRepository.saveStock(it)
        }
    }

}
