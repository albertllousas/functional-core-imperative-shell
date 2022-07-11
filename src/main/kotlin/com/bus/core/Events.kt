package com.bus.core

sealed interface DomainEvent

data class SeatsReserved(val busTrip: BusTrip, val seatCodes: List<SeatCode>): DomainEvent