package no.nav.personbruker.dittnav.api.config

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get

fun Routing.healthApi() {

    val pingJsonResponse = """{"ping": "pong"}"""

    get("/person/dittnav-api/internal/isAlive") {
        call.respondText(text = "ALIVE", contentType = ContentType.Text.Plain)
    }

    get("/person/dittnav-api/internal/isReady") {
        call.respondText(text = "READY", contentType = ContentType.Text.Plain)
    }

    get("/person/dittnav-api/internal/ping") {
        call.respondText(pingJsonResponse, ContentType.Application.Json)
    }

}
