package com.bus.shell.db

import arrow.core.left
import arrow.core.right
import com.bus.core.BusTripId
import com.bus.core.BusTripNotFound
import com.bus.core.SideEffects
import com.bus.fixtures.BusTripBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class InMemoryBusTripRepositoryShould {

    private val busTrip = BusTripBuilder.build()

    private val busTripRepository: SideEffects.BusTripRepository = InMemoryBusTripRepository(
        mutableMapOf(busTrip.id to busTrip)
    )

    @Test
    fun `find a bus trip`() {
        assertThat(busTripRepository.find(busTrip.id)).isEqualTo(busTrip.right())
    }

    @Test
    fun `fail finding a non existent bus trip`() {
        assertThat(busTripRepository.find(BusTripId(UUID.randomUUID()))).isEqualTo(BusTripNotFound.left())
    }

    @Test
    fun `save a bus trip`() {
        val newBusTrip = BusTripBuilder.build()

        busTripRepository.save(newBusTrip)

        assertThat(busTripRepository.find(newBusTrip.id)).isEqualTo(newBusTrip.right())
    }
}