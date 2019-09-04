package no.nav.personbruker.dittnav.api.api

import io.ktor.application.call
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.HttpMethod
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import org.json.simple.JSONObject

fun Route.ubehandledeMeldinger(httpClient: HttpClient, dittNAVLegacyURL: String) {
    get("/meldinger/ubehandlede") {
        val authHeader = call.request.parseAuthorizationHeader()?.render()
        if (authHeader != null) {
            val ubehandledeMeldinger = httpClient.use { client ->
                client.request<JSONObject> {
                    url(dittNAVLegacyURL + "meldinger/ubehandlede")
                    method = HttpMethod.Get
                    header(Authorization, authHeader)
                }
            }
            call.respondText(text =  ubehandledeMeldinger.toString(), contentType = ContentType.Application.Json)
        }
    }
}
