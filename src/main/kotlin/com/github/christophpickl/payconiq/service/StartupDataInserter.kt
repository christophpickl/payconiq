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
            newStock("NASDAQ:AAPL", 868),
            newStock("NASDAQ:GOOGL", 720),
            newStock("NASDAQ:MSFT", 600),
            newStock("NASDAQ:AMZN", 560),
            newStock("NASDAQ:FB", 508)
        ).forEach {
            stocksRepository.saveStock(it)
        }
    }

    private fun newStock(name: String, currentPrice: Int) = StockDbo(
        id = UNSET_STOCK_ID,
        name = name,
        currentPrice = AmountDbo.euro(currentPrice),
        lastUpdate = LocalDateTime.now()
    )

}
