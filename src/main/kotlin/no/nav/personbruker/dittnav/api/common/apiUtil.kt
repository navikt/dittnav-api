package no.nav.personbruker.dittnav.api.common

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpHeaders
import io.ktor.util.pipeline.PipelineContext

fun PipelineContext<Unit, ApplicationCall>.extractTokenFromRequest(): String {
    var authToken = getTokenFromHeader()
    if (authToken == null) {
        authToken = getTokenFromCookie()
    }
    if (authToken == null) {
        throw Exception("Det ble ikke funnet noe token. Dette skal ikke kunne skje.")
    }
    return authToken
}

private fun PipelineContext<Unit, ApplicationCall>.getTokenFromHeader() =
        call.request.headers[HttpHeaders.Authorization]?.replace("Bearer ", "")

fun PipelineContext<Unit, ApplicationCall>.getTokenFromCookie() =
        call.request.cookies["selvbetjening-idtoken"]
