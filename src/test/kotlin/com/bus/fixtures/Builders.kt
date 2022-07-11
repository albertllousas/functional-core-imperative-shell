package com.bus.fixtures

import com.bus.core.Bus
import com.bus.core.BusId
import com.bus.core.BusTrip
import com.bus.core.BusTripId
import com.bus.core.Seat
import com.bus.core.SeatCode
import com.bus.core.SeatType
import com.bus.core.SeatingRow
import com.github.javafaker.Faker
import java.util.UUID

private val faker = Faker()

object BusTripBuilder {

    fun build(
        id: UUID = UUID.randomUUID(),
        bus: Bus = BusBuilder.build(),
        reservedSeats: List<SeatCode> = emptyList()
    ) = BusTrip(
        id = BusTripId(id),
        bus = bus,
        reservedSeats = reservedSeats
    )
}

object BusBuilder {

    fun build(
        id: UUID = UUID.randomUUID(),
        seatingChart : List<SeatingRow> = listOf(
            SeatingRow(number=1, listOf(Seat(SeatCode(faker.code().ean8()),SeatType.AISLE)))
        )
    ) = Bus(
        id = BusId(id),
        seatingChart = seatingChart
    )
}
