package com.bus.core

import arrow.core.Either
import arrow.core.flatten
import arrow.core.left
import arrow.core.right
import java.util.UUID

@JvmInline
value class BusId(val value: UUID)

data class Bus(val id: BusId, val seatingChart: List<SeatingRow>) {

    companion object {
        fun findSeat(bus: Bus, seatCode: SeatCode): Either<SeatNotFound, Seat> =
            bus.seatingChart.map { it.seats }.flatten().find { it.code == seatCode }?.right() ?: SeatNotFound.left()
    }
}

data class SeatingRow(val number: Int, val seats: List<Seat>)

data class Seat(val code: SeatCode, val type: SeatType)

enum class SeatType {
    AISLE, MIDDLE, WINDOW
}
