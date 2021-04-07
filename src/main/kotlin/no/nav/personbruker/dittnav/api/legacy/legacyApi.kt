package no.nav.personbruker.dittnav.api.legacy

import io.ktor.application.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.SocketTimeoutException

val log = LoggerFactory.getLogger(LegacyConsumer::class.java)

fun Route.legacyApi(legacyConsumer: LegacyConsumer) {

    val ubehandledeMeldingerPath = "/meldinger/ubehandlede"
    val paabegynteSakerPath = "/saker/paabegynte"
    val sakstemaPath = "/saker/sakstema"
    val navnPath = "/personalia/navn"
    val identPath = "/personalia/ident"
    val meldekortPath = "/meldekortinfo"
    val oppfolgingPath = "/oppfolging"

    get(ubehandledeMeldingerPath) {
        log.logWhenTokenIsAboutToExpire(authenticatedUser)
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
        log.warn("Forbindelsen mot legacy-endepunkt '$path' har utgått. Feilmelding: [${e.message}]. $authenticatedUser", e)
        call.respond(HttpStatusCode.GatewayTimeout)
    } catch (e: Exception) {
        log.warn("Det skjedde en feil mot legacy-endepunkt '$path'. Feilmelding: [${e.message}]. $authenticatedUser", e)
        call.respond(HttpStatusCode.InternalServerError)
    }

}

fun Logger.logWhenTokenIsAboutToExpire(user: AuthenticatedUser) {
    val expiryThresholdInSeconds = 30L

    if (user.isTokenAboutToExpire(expiryThresholdInSeconds)) {
        info("Det er mindre enn $expiryThresholdInSeconds sekunder før token-et går ut for: $user")
    }
}
