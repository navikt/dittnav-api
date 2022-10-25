package no.nav.personbruker.dittnav.api.meldekort

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.meldekortApi(meldekortConsumer: MeldekortConsumer) {

    val log = KotlinLogging.logger { }

    get("/meldekortinfo") {
        try {
            val meldekortInfo = meldekortConsumer.getMeldekortInfo(authenticatedUser)

            call.respond(meldekortInfo)
        } catch (e: Exception) {
            log.warn("Det skjedde en feil mot meldekort. Feilmelding: [${e.message}]. $authenticatedUser", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    get("/meldekortstatus") {
        try {
            val meldekortStatus = meldekortConsumer.getMeldekortStatus(authenticatedUser)

            call.respond(meldekortStatus)
        } catch (e: Exception) {
            log.warn("Det skjedde en feil mot meldekort. Feilmelding: [${e.message}]. $authenticatedUser", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}

