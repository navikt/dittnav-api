package no.nav.personbruker.dittnav.api.legacy

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.util.pipeline.PipelineContext
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory
import java.net.SocketTimeoutException

val log = LoggerFactory.getLogger(object{}::class.java.`package`.name)

fun Route.legacyApi(legacyConsumer: LegacyConsumer) {

    val ubehandledeMeldingerPath = "/meldinger/ubehandlede"
    val paabegynteSakerPath = "/saker/paabegynte"
    val sakstemaPath = "/saker/sakstema"
    val navnPath = "/personalia/navn"
    val identPath = "/personalia/ident"
    val meldekortPath = "/meldekortinfo"
    val oppfolgingPath = "/oppfolging"

    get(ubehandledeMeldingerPath) {
        logWhenTokenIsAboutToExpire(authenticatedUser)
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, ubehandledeMeldingerPath)
    }

    get(paabegynteSakerPath) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, paabegynteSakerPath)
    }

    get(sakstemaPath) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, sakstemaPath)
    }

    get(navnPath) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, navnPath)
    }

    get(identPath) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, identPath)
    }

    get(meldekortPath) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, meldekortPath)
    }

    get(oppfolgingPath) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, oppfolgingPath)
    }

}

private suspend fun PipelineContext<Unit, ApplicationCall>.hentRaattFraLegacyApiOgReturnerResponsen(consumer: LegacyConsumer, path: String) {
    try {
        val response = consumer.getLegacyContent(path, authenticatedUser)
        call.respond(response.status, response.readBytes())
    } catch (e: SocketTimeoutException) {
        log.warn("Forbindelsen mot legacy-endepunkt '$path' har utgått. Feilmelding: [${e.message}]")
        call.respond(HttpStatusCode.GatewayTimeout)
    } catch (e: Exception) {
        log.warn("Det skjedde en feil mot legacy-endepunkt '$path'. Feilmelding: [${e.message}]")
        call.respond(HttpStatusCode.InternalServerError)
    }

}

fun logWhenTokenIsAboutToExpire(user: AuthenticatedUser) {
    val expiryThresholdInMinutes = 2L

    if (user.isTokenAboutToExpire(expiryThresholdInMinutes)) {
        log.info("Det er mindre enn $expiryThresholdInMinutes minutter før token-et går ut for: $user")
    }
}
