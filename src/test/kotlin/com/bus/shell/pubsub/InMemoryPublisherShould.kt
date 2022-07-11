package com.bus.shell.pubsub

import com.bus.core.BusTripNotFound
import com.bus.core.SeatsReserved
import com.bus.core.SideEffects
import com.bus.fixtures.BusTripBuilder
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class InMemoryPublisherShould {
    @Test
    fun `notify domain event to the handlers`() {
        val event = SeatsReserved(busTrip = BusTripBuilder.build(), seatCodes = emptyList())
        val firstHandler = mockk<SideEffects.HandleEvent>(relaxed = true)
        val secondHandler = mockk<SideEffects.HandleEvent>(relaxed = true)
        val thirdHandler = mockk<SideEffects.HandleEvent>(relaxed = true)
        val publish = InMemoryPublisher(
            listOf(firstHandler, secondHandler, thirdHandler),
            emptyList()
        )

        publish(event)

        verify {
            firstHandler.invoke(event)
            secondHandler.invoke(event)
            thirdHandler.invoke(event)
        }
    }

    @Test
    fun `notify domain error to the handlers`() {
        val busTripNotFound = BusTripNotFound
        val firstHandler = mockk<SideEffects.HandleError>(relaxed = true)
        val secondHandler = mockk<SideEffects.HandleError>(relaxed = true)
        val thirdHandler = mockk<SideEffects.HandleError>(relaxed = true)
        val publish = InMemoryPublisher(
            emptyList(),
            listOf(firstHandler, secondHandler, thirdHandler),
        )

        publish(busTripNotFound)

        verify {
            firstHandler.invoke(busTripNotFound)
            secondHandler.invoke(busTripNotFound)
            thirdHandler.invoke(busTripNotFound)
        }
    }
}