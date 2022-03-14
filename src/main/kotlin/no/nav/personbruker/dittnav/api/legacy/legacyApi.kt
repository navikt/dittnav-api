package no.nav.personbruker.dittnav.api.legacy

import io.ktor.application.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.config.executeOnUnexpiredTokensOnly
import org.slf4j.LoggerFactory
import java.net.SocketTimeoutException

val log = LoggerFactory.getLogger(LegacyConsumer::class.java)

fun Route.legacyApi(legacyConsumer: LegacyConsumer) {

    get(LegacyApiOperations.PAABEGYNTESAKER.path) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, LegacyApiOperations.PAABEGYNTESAKER)
    }

    get(LegacyApiOperations.SAKSTEMA.path) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, LegacyApiOperations.SAKSTEMA)
    }

}

private suspend fun PipelineContext<Unit, ApplicationCall>.hentRaattFraLegacyApiOgReturnerResponsen(consumer: LegacyConsumer, operation: LegacyApiOperations) {
    executeOnUnexpiredTokensOnly {
        try {
            val response = consumer.getLegacyContent(operation, authenticatedUser)
            call.respond(response.status, response.readBytes())
        } catch (e: SocketTimeoutException) {
            log.warn("Forbindelsen mot legacy-endepunkt '$operation' har utgått. Feilmelding: [${e.message}]. $authenticatedUser", e)
            call.respond(HttpStatusCode.GatewayTimeout)
        } catch (e: Exception) {
            log.warn("Det skjedde en feil mot legacy-endepunkt '$operation'. Feilmelding: [${e.message}]. $authenticatedUser", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}
