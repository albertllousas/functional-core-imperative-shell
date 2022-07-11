package com.bus.shell.entrypoints.http

import arrow.core.left
import arrow.core.right
import com.bus.core.AlreadyReservedSeat
import com.bus.core.BusTripId
import com.bus.core.BusTripNotFound
import com.bus.core.ReserveSeats
import com.bus.core.SeatCode
import com.bus.core.SeatNotFound
import com.bus.core.SeatsReserved
import com.bus.core.SideEffects
import com.bus.fixtures.BusTripBuilder
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpHeaders.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.serialization.jackson.jackson
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.Routing
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import java.util.UUID

class ReserveSeatsOnBusTripRouteShould {

    private val busTripRepository = mockk<SideEffects.BusTripRepository>(relaxed = true)

    private val publishEvent = mockk<SideEffects.PublishEvent>(relaxed = true)

    private val publishError = mockk<SideEffects.PublishError>(relaxed = true)

    private val reserveSeats = mockk<ReserveSeats>()

    @Test
    fun `should reserve seats on a bus trip`() = withTestApp {
        val busTripId = UUID.randomUUID()
        val busTrip = BusTripBuilder.build()
        coEvery { busTripRepository.find(BusTripId(busTripId)) } returns busTrip.right()
        coEvery { reserveSeats(busTrip, listOf(SeatCode("1g"))) } returns busTrip.right()

        val response = client.patch("/bus-trips/$busTripId/seats/reserve") {
            header(ContentType, Application.Json)
            setBody(""" {"seats": ["1g"] } """)
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        verify {
            busTripRepository.save(busTrip)
            publishEvent(SeatsReserved(busTrip, listOf(SeatCode("1g"))))
        }
    }

    @Test
    fun `should fail when reserving seats on a bus is not possible`() = withTestApp {
        val busTripId = UUID.randomUUID()
        coEvery { busTripRepository.find(BusTripId(busTripId)) } returns BusTripNotFound.left()

        val response = client.patch("/bus-trips/$busTripId/seats/reserve") {
            header(ContentType, Application.Json)
            setBody(""" {"seats": ["1g"] } """)
        }

        assertThat(response.status).isNotEqualTo(HttpStatusCode.OK)
        verify { publishError(any()) }
    }

    @TestFactory
    fun httpErrors() =
        listOf(
            Triple(BusTripNotFound, NotFound, HttpError("Bus trip not found")),
            Triple(SeatNotFound, NotFound, HttpError("Seat not found")),
            Triple(AlreadyReservedSeat, Conflict, HttpError("Already reserved"))
        ).map { (error, expectedCode, expectedMsg) ->
            dynamicTest("map ${error::class.simpleName} to an http error") {
                assertThat(error.asHttpError()).isEqualTo(Pair(expectedCode, expectedMsg))
            }
        }


    private fun withTestApp(callback: suspend ApplicationTestBuilder.() -> Unit) =
        testApplication {
            install(ContentNegotiation) { jackson() }
            install(Routing) { reserveRoute(busTripRepository, publishEvent, publishError, reserveSeats) }
            callback()
        }
}
