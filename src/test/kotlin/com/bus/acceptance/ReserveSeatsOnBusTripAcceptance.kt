package com.bus.acceptance

import com.bus.fixtures.BusTripBuilder
import com.bus.shell.appModules
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpHeaders.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ReserveSeatsOnBusTripAcceptance {

    private val busTrip = BusTripBuilder.build()

    private val testData = mutableMapOf(busTrip.id to busTrip)

    @Test
    fun `should reserve seats on a bus trip`() = withTestApp {

        val response = client.patch("/bus-trips/${busTrip.id.value}/seats/reserve") {
            header(ContentType, Application.Json)
            setBody(""" {"seats": ["${busTrip.bus.seatingChart[0].seats[0].code.value}"] } """)
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
    }

    private fun withTestApp(callback: suspend ApplicationTestBuilder.() -> Unit) =
        testApplication {
            application {
                appModules(testData)
            }
            callback()
        }
}
