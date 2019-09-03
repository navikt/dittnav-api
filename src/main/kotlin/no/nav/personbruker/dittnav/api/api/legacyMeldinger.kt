package no.nav.personbruker.dittnav.api.api

import io.ktor.application.call
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.config.Environment
import org.json.simple.JSONObject

fun Route.legacyMeldinger(environment: Environment, httpClient: HttpClient) {
    get("/meldinger/ubehandlede") {
        val authHeader = call.request.parseAuthorizationHeader()?.render()
        if (authHeader != null) {
            val ubehandledeMeldinger = httpClient.use { client ->
                client.request<JSONObject> {
                    url(environment.dittNAVLegacyURL + "meldinger/ubehandlede")
                    method = HttpMethod.Get
                    header(Authorization, authHeader)
                }
            }
            call.respond(HttpStatusCode(200, "OK"), ubehandledeMeldinger)
        }
    }
}
