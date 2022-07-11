package com.bus.core

import arrow.core.left
import arrow.core.right
import com.bus.fixtures.BusBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class BusShould {

    @Test
    fun `find a seat`() {
        val bus = BusBuilder.build()
        val seat = bus.seatingChart[0].seats[0]

        val result = Bus.findSeat(bus, seat.code)

        assertThat(result).isEqualTo(seat.right())
    }

    @Test
    fun `fail finding a non existent seat`() {
        val bus = BusBuilder.build()

        val result = Bus.findSeat(bus, SeatCode("invalid"))

        assertThat(result).isEqualTo(SeatNotFound.left())
    }
}
