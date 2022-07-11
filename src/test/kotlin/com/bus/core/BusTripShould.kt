package com.bus.core

import arrow.core.left
import arrow.core.right
import com.bus.fixtures.BusTripBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BusTripShould {

    @Test
    fun `reserve a seat in a bus`() {
        val busTrip = BusTripBuilder.build()
        val seatCode = busTrip.bus.seatingChart[0].seats[0].code

        val result = BusTrip.reserveSeats(busTrip, listOf(seatCode))

        assertThat(result).isEqualTo(busTrip.copy(reservedSeats = listOf(seatCode)).right())
    }

    @Test
    fun `fail reserving a seat in a bus when seat does not exists`() {
        val busTrip = BusTripBuilder.build()

        val result = BusTrip.reserveSeats(busTrip, listOf(SeatCode("non-existent")))

        assertThat(result).isEqualTo(SeatNotFound.left())
    }

    @Test
    fun `fail reserving a seat in a bus when it was already reserved`() {
        val busTrip = BusTripBuilder.build()
        val seatCode = busTrip.bus.seatingChart[0].seats[0].code
        val busTripWithReservations = busTrip.copy(reservedSeats = listOf(seatCode))


        val result = BusTrip.reserveSeats(busTripWithReservations, listOf(seatCode))

        assertThat(result).isEqualTo(AlreadyReservedSeat.left())
    }
}
