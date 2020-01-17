package no.nav.personbruker.dittnav.api.health

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.authenticationCheck() {

    val pingJsonResponse = """{"ping": "pong"}"""

    get("/authPing") {
        call.respondText(pingJsonResponse, ContentType.Application.Json)
    }
}
