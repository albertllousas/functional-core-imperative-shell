package com.bus.core

sealed interface DomainError

object BusTripNotFound : DomainError

object SeatNotFound : DomainError

object AlreadyReservedSeat : DomainError
