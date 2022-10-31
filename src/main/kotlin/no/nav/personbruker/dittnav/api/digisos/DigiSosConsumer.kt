package no.nav.personbruker.dittnav.api.digisos


import io.ktor.client.HttpClient
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
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.config.ProduceEventException
import no.nav.personbruker.dittnav.api.config.get

import java.net.URL

class DigiSosConsumer(
    private val client: HttpClient,
    private val tokendings: DigiSosTokendings,
    digiSosSoknadBaseURL: URL
) {

    private val log = KotlinLogging.logger { }

    private val aktivePaabegynteEndpoint = URL("$digiSosSoknadBaseURL/dittnav/pabegynte/aktive")
    private val inaktivePaabegynteEndpoint = URL("$digiSosSoknadBaseURL/dittnav/pabegynte/inaktive")
    private val donePaabegynteEndpoint = URL("$digiSosSoknadBaseURL/dittnav/pabegynte/lest")


    suspend fun getPaabegynteActive(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> =
        wrapAsMultiSourceResult(user) {
            val token = tokendings.exchangeToken(user)
            client.get<List<Paabegynte>>(aktivePaabegynteEndpoint, token).toInternals()
        }


    suspend fun getPaabegynteInactive(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> =
        wrapAsMultiSourceResult(user) {
            val token = tokendings.exchangeToken(user)
            client.get<List<Paabegynte>>(inaktivePaabegynteEndpoint, token).toInternals()
        }

    suspend fun markEventAsDone(user: AuthenticatedUser, dto: DoneDTO): HttpResponse {
        val res = runCatching {
            val token = tokendings.exchangeToken(user)
            post(donePaabegynteEndpoint, dto, token)

        }.onFailure { cause ->
            throw ProduceEventException("Klarte ikke å markere event hos DigiSos som lest.", cause)
        }

        return res.getOrThrow()
            .also {
                if (it.status != HttpStatusCode.OK) {
                    log.warn("Feil mot $donePaabegynteEndpoint: ${it.status.value} ${it.status.description}")
                }
            }
    }


    private suspend fun <T> wrapAsMultiSourceResult(
        user: AuthenticatedUser,
        getEvents: suspend (AuthenticatedUser) -> List<T>
    ): MultiSourceResult<T, KildeType> {
        return try {
            val results = getEvents(user)
            MultiSourceResult.createSuccessfulResult(results, KildeType.DIGISOS)

        } catch (e: Exception) {
            log.warn("Klarte ikke å hente data fra DigiSos: $e", e)
            MultiSourceResult.createErrorResult(KildeType.DIGISOS)
        }
    }

    private suspend fun post(url: URL, done: DoneDTO, token: String) =
        withContext(Dispatchers.IO) {
            client.post {
                url(url)
                method = HttpMethod.Post
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(done)
            }
        }

}
