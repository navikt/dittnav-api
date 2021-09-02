package no.nav.personbruker.dittnav.api.digisos

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory
import java.net.URL

class DigiSosClient(
    private val client: HttpClient,
    digiSosSoknadBaseURL: URL,
    digiSosInnsynBaseURL: URL,
) {

    private val log = LoggerFactory.getLogger(DigiSosClient::class.java)

    private val aktivePaabegynteEndpoint = URL("$digiSosSoknadBaseURL/dittnav/pabegynte/aktive")
    private val inaktivePaabegynteEndpoint = URL("$digiSosSoknadBaseURL/dittnav/pabegynte/inaktive")
    private val donePaabegynteEndpoint = URL("$digiSosSoknadBaseURL/dittnav/pabegynte/lest")

    private val aktiveEttersendelserEndpoint = URL("$digiSosInnsynBaseURL/dittnav/oppgaver/aktive")
    private val inaktiveEttersendelserEndpoint = URL("$digiSosInnsynBaseURL/dittnav/oppgaver/inaktive")

    suspend fun getPaabegynteActive(user: AuthenticatedUser): List<Paabegynte> {
        return client.get(aktivePaabegynteEndpoint, user)
    }

    suspend fun getPaabegynteInactive(user: AuthenticatedUser): List<Paabegynte> {
        return client.get(inaktivePaabegynteEndpoint, user)
    }

    suspend fun getEttersendelserActive(user: AuthenticatedUser): List<Ettersendelse> {
        return client.get(aktiveEttersendelserEndpoint, user)
    }

    suspend fun getEttersendelserInactive(user: AuthenticatedUser): List<Ettersendelse> {
        return client.get(inaktiveEttersendelserEndpoint, user)
    }

    suspend fun markEventAsDone(user: AuthenticatedUser, done: DoneDTO): HttpResponse {
        val response: HttpResponse = post(donePaabegynteEndpoint, done, user)

        if (response.status != HttpStatusCode.OK) {
            log.warn("Feil mot $donePaabegynteEndpoint: ${response.status.value} ${response.status.description}")
        }

        return response
    }

    private suspend inline fun <reified T> post(url: URL, done: DoneDTO, user: AuthenticatedUser): T =
        withContext(Dispatchers.IO) {
            client.post {
                url(url)
                method = HttpMethod.Post
                header(HttpHeaders.Authorization, user.createAuthenticationHeader())
                contentType(ContentType.Application.Json)
                body = done
            }
        }

}
