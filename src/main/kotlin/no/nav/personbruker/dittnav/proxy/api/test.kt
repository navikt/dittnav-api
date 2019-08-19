package no.nav.personbruker.dittnav.proxy.api

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.test() {

    get("/test") {
        call.respondText(text = "TEST", contentType = ContentType.Text.Plain)
    }

}
