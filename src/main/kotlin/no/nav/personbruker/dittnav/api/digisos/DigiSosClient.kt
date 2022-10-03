package no.nav.personbruker.dittnav.api.digisos


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.config.get

import java.net.URL

class DigiSosClient(
    private val client: HttpClient,
    digiSosSoknadBaseURL: URL
) {

    private val log = KotlinLogging.logger {  }


    private val aktivePaabegynteEndpoint = URL("$digiSosSoknadBaseURL/dittnav/pabegynte/aktive")
    private val inaktivePaabegynteEndpoint = URL("$digiSosSoknadBaseURL/dittnav/pabegynte/inaktive")
    private val donePaabegynteEndpoint = URL("$digiSosSoknadBaseURL/dittnav/pabegynte/lest")

    suspend fun getPaabegynteActive(user: AuthenticatedUser): List<BeskjedDTO> {
        val external = client.get<List<Paabegynte>>(aktivePaabegynteEndpoint, user)
        return external.toInternals()
    }

    suspend fun getPaabegynteInactive(user: AuthenticatedUser): List<BeskjedDTO> {
        val externals = client.get<List<Paabegynte>>(inaktivePaabegynteEndpoint, user)
        return externals.toInternals()
    }

    suspend fun markEventAsDone(user: AuthenticatedUser, done: DoneDTO): HttpResponse {
        val response: HttpResponse = post(donePaabegynteEndpoint, done, user)

        if (response.status != HttpStatusCode.OK) {
            log.warn("Feil mot $donePaabegynteEndpoint: ${response.status.value} ${response.status.description}")
        }

        return response.body()
    }

    private suspend inline fun <reified T> post(url: URL, done: DoneDTO, user: AuthenticatedUser): T =
        withContext(Dispatchers.IO) {
            client.post {
                url(url)
                method = HttpMethod.Post
                header(HttpHeaders.Authorization, user.createAuthenticationHeader())
                contentType(ContentType.Application.Json)
                setBody(done)
            }
        }.body()

}
