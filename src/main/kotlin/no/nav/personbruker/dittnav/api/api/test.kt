package no.nav.personbruker.dittnav.api.api

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.client.features.json.JsonFeature

fun Route.test() {

    get("/test") {
        call.respondText(text = "TEST", contentType = ContentType.Text.Plain)
    }

}
