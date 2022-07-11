package com.bus.core

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.core.sequence
import java.util.UUID

@JvmInline
value class BusTripId(val value: UUID)

@JvmInline
value class SeatCode(val value: String)

typealias ReserveSeats = (BusTrip, List<SeatCode>) -> Either<DomainError, BusTrip>

data class BusTrip(val id: BusTripId, val bus: Bus, val reservedSeats: List<SeatCode>) {

    private fun isReserved(seatCode: SeatCode) = reservedSeats.contains(seatCode)

    companion object {

        val reserveSeats: ReserveSeats = { busTrip, seatsCodes ->
            seatsCodes
                .map { Bus.findSeat(busTrip.bus, it) }
                .sequence()
                .flatMap { seats ->
                    if( seats.any {  busTrip.isReserved(it.code) }) AlreadyReservedSeat.left()
                    else busTrip.copy(reservedSeats = busTrip.reservedSeats + seatsCodes).right()
                }
        }
    }
}
