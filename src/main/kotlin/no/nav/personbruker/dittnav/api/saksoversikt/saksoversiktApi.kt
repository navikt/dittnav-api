package no.nav.personbruker.dittnav.api.saksoversikt

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import org.slf4j.LoggerFactory

fun Route.saksoversiktApi(saksoversiktService: SaksoversiktService) {

    val log = LoggerFactory.getLogger("saksoversiktApi")

    get("/saker/sakstema") {
        call.respond(SakstemaWrapper.empty())
    }

    get("/saker/paabegynte") {
        try {
            val oppfolging = saksoversiktService.getPaabegynte(authenticatedUser)

            call.respond(oppfolging)
        } catch (e: Exception) {
            log.warn("Det skjedde en feil mot saksoverikt. Feilmelding: [${e.message}]. $authenticatedUser", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}
