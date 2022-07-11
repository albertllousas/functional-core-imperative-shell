package com.bus.shell

import com.bus.core.BusTrip
import com.bus.core.BusTripId
import com.bus.core.SideEffects
import com.bus.shell.db.InMemoryBusTripRepository
import com.bus.shell.entrypoints.http.reserveRoute
import com.bus.shell.pubsub.HandleLogging
import com.bus.shell.pubsub.InMemoryPublisher
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.Routing

fun main(): Unit = run { embeddedServer(Netty, port = 8080, module = Application::appModules).start(wait = true) }


fun Application.appModules(testDataSet: MutableMap<BusTripId, BusTrip> = HashMap()) {
    val busTripRepository: SideEffects.BusTripRepository = InMemoryBusTripRepository(testDataSet)
    val handleLogging = HandleLogging()
    val publisher = InMemoryPublisher(eventHandlers = listOf(handleLogging), errorHandlers = listOf(handleLogging))

    install(ContentNegotiation) { jackson() }
    install(Routing) {
        reserveRoute(busTripRepository, publisher, publisher, BusTrip.reserveSeats::invoke)
    }
}
