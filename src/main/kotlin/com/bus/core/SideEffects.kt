package com.bus.core

import arrow.core.Either

object SideEffects {

    interface BusTripRepository {
        fun find(id: BusTripId): Either<BusTripNotFound, BusTrip>
        fun save(busTrip: BusTrip)
    }

    interface PublishEvent {
        operator fun invoke(event: DomainEvent)
    }

    interface PublishError {
        operator fun invoke(error: DomainError)
    }

    interface HandleEvent {
        operator fun invoke(event: DomainEvent)
    }

    interface HandleError {
        operator fun invoke(error: DomainError)
    }
}