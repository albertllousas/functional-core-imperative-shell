package com.bus.shell.db

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.bus.core.BusTrip
import com.bus.core.BusTripId
import com.bus.core.BusTripNotFound
import com.bus.core.SideEffects

class InMemoryBusTripRepository(
    private val dataset: MutableMap<BusTripId, BusTrip> = HashMap(),
) : SideEffects.BusTripRepository {

    override fun find(id: BusTripId): Either<BusTripNotFound, BusTrip> = dataset[id]?.right() ?: BusTripNotFound.left()

    override fun save(busTrip: BusTrip): Unit = run { dataset[busTrip.id] = busTrip }
}