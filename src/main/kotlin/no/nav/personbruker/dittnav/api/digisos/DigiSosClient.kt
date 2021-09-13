package no.nav.personbruker.dittnav.api.digisos

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.oppgave.OppgaveDTO
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

    suspend fun getPaabegynteActive(user: AuthenticatedUser): List<BeskjedDTO> {
        val external = client.get<List<Paabegynte>>(aktivePaabegynteEndpoint, user)
        return external.toInternals()
    }

    suspend fun getPaabegynteInactive(user: AuthenticatedUser): List<BeskjedDTO> {
        val externals = client.get<List<Paabegynte>>(inaktivePaabegynteEndpoint, user)
        return externals.toInternals()
    }

    suspend fun getEttersendelserActive(user: AuthenticatedUser): List<OppgaveDTO> {
        val externals = client.get<List<Ettersendelse>>(aktiveEttersendelserEndpoint, user)
        return externals.toInternals()
    }

    suspend fun getEttersendelserInactive(user: AuthenticatedUser): List<OppgaveDTO> {
        val externals = client.get<List<Ettersendelse>>(inaktiveEttersendelserEndpoint, user)
        return externals.toInternals()
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
