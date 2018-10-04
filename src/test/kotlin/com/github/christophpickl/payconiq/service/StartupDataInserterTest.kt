package com.github.christophpickl.payconiq.service

import com.github.christophpickl.payconiq.persistence.StocksRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeast
import org.mockito.Mockito.never
import org.springframework.core.env.Environment

@ExtendWith(MockitoExtension::class)
class StartupDataInserterTest {

    @Mock
    private lateinit var stocksRepo: StocksRepository
    @Mock
    private lateinit var environment: Environment

    @Test
    fun `Given test profile is active When push application ready event Then at least one stock is saved in repository`() {
        whenever(environment.activeProfiles).thenReturn(emptyArray())

        testee.onApplicationReadyEvent()

        verify(stocksRepo, atLeast(1)).saveStock(any())
    }

    @Test
    fun `Given no profile is active When push application ready event Then no stock is saved in repository`() {
        whenever(environment.activeProfiles).thenReturn(arrayOf("test"))

        testee.onApplicationReadyEvent()

        verify(stocksRepo, never()).saveStock(any())
    }

    private val testee get() = StartupDataInserter(stocksRepo, environment)

}
