package com.bus.shell.pubsub

import com.bus.core.BusTripNotFound
import com.bus.core.SeatsReserved
import com.bus.fixtures.BusTripBuilder
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.slf4j.helpers.NOPLogger

class HandleLoggingShould {
    private val logger = spyk(NOPLogger.NOP_LOGGER)

    private val handleLogging = HandleLogging(logger)

    @Test
    fun `handle a domain event writing a log`() {
        val event = SeatsReserved(busTrip = BusTripBuilder.build(), seatCodes = emptyList())

        handleLogging(event)

        verify { logger.info("""domain-event: 'SeatsReserved'""") }
    }

    @Test
    fun `handle a domain error writing a log`() {
        val error = BusTripNotFound

        handleLogging(error)

        verify { logger.error("""domain-error: 'BusTripNotFound'""") }
    }
}
