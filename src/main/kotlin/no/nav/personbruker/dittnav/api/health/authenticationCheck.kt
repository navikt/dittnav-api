package no.nav.personbruker.dittnav.api.health

import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get


fun Route.authenticationCheck() {

    val pingJsonResponse = """{"ping": "pong"}"""

    get("/authPing") {
            call.respondText(pingJsonResponse, ContentType.Application.Json)
        }
}
