package com.bus.shell.entrypoints.http

import arrow.core.flatMap
import com.bus.core.AlreadyReservedSeat
import com.bus.core.BusTripId
import com.bus.core.BusTripNotFound
import com.bus.core.DomainError
import com.bus.core.ReserveSeats
import com.bus.core.SeatCode
import com.bus.core.SeatNotFound
import com.bus.core.SeatsReserved
import com.bus.core.SideEffects
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.patch
import io.ktor.server.routing.route
import java.util.UUID

fun Route.reserveRoute(
    busTripRepository: SideEffects.BusTripRepository,
    publishEvent: SideEffects.PublishEvent,
    publishError: SideEffects.PublishError,
    reserveSeats: ReserveSeats,
): Route = route("/bus-trips/{id}/seats/reserve") {

    patch {

        val busTripId = BusTripId(UUID.fromString(call.parameters["id"]))
        val seatCodes = call.receive<ReserveSeatsHttpRequest>().seats.map(::SeatCode)

        busTripRepository.find(busTripId)
            .flatMap { reserveSeats(it, seatCodes) }
            .tap(busTripRepository::save)
            .map { SeatsReserved(it, seatCodes) }
            .tap { publishEvent(it) }
            .tapLeft { publishError(it) }
            .fold(
                ifRight = { call.respond(OK) },
                ifLeft = { it.asHttpError().let { (code, msg) -> call.respond(code, msg) } }
            )
    }
}

fun DomainError.asHttpError(): Pair<HttpStatusCode, HttpError> = when (this) {
    BusTripNotFound -> Pair(NotFound, HttpError("Bus trip not found"))
    SeatNotFound -> Pair(NotFound, HttpError("Seat not found"))
    AlreadyReservedSeat -> Pair(Conflict, HttpError("Already reserved"))
}

data class HttpError(val details: String)

data class ReserveSeatsHttpRequest(val seats: List<String>)
