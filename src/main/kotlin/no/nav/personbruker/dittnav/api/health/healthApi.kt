package no.nav.personbruker.dittnav.api.health

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat

fun Routing.healthApi(dependencyPinger: DependencyPinger, collectorRegistry: CollectorRegistry = CollectorRegistry.defaultRegistry) {

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
        call.pingDependencies(dependencyPinger)
    }

    get("/metrics") {
        val names = call.request.queryParameters.getAll("name")?.toSet() ?: emptySet()
        call.respondTextWriter(ContentType.parse(TextFormat.CONTENT_TYPE_004), HttpStatusCode.OK) {
            TextFormat.write004(this, collectorRegistry.filteredMetricFamilySamples(names))
        }
    }

}
