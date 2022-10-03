package no.nav.personbruker.dittnav.api.health

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.micrometer.prometheus.PrometheusMeterRegistry

fun Route.healthApi(collectorRegistry: PrometheusMeterRegistry) {

    val pingJsonResponse = """{"ping": "pong"}"""

    get("/internal/ping") {
        call.respondText(pingJsonResponse, ContentType.Application.Json)
    }

    get("/internal/isAlive") {
        call.respondText(text = "ALIVE", contentType = ContentType.Text.Plain)
    }

    get("/internal/isReady") {
        call.respondText(text = "READY", contentType = ContentType.Text.Plain)
    }

    get("/internal/selftest") {
        call.respond(HttpStatusCode.OK)
    }

    get("/metrics") {
        call.respond(collectorRegistry.scrape())
    }

}
