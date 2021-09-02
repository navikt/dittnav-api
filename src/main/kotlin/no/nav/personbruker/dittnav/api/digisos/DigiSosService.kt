package no.nav.personbruker.dittnav.api.digisos

import io.ktor.client.statement.*
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.common.ProduceEventException
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory

class DigiSosService(private val digiSosClient: DigiSosClient) {

    private val log = LoggerFactory.getLogger(DigiSosService::class.java)

    suspend fun getPaabegynteActive(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        return fetchEvents(user) {
            digiSosClient.getPaabegynteActive(user)
        }
    }

    suspend fun getPaabegynteInactive(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        return fetchEvents(user) {
            digiSosClient.getPaabegynteInactive(user)
        }
    }

    suspend fun markEventAsDone(user: AuthenticatedUser, dto: DoneDTO): HttpResponse {
        val res = runCatching {
            digiSosClient.markEventAsDone(user, dto)

        }.onFailure { cause ->
            throw ProduceEventException("Klarte ikke å markere event hos DigiSos som lest.", cause)
        }

        return res.getOrThrow()
    }

    private suspend fun fetchEvents(
        user: AuthenticatedUser,
        getEvents: suspend (AuthenticatedUser) -> List<BeskjedDTO>
    ): MultiSourceResult<BeskjedDTO, KildeType> {
        return try {
            val results = getEvents(user)
            MultiSourceResult.createSuccessfulResult(results, KildeType.DIGISOS)

        } catch (e: Exception) {
            log.warn("Klarte ikke å hente data fra DigiSos: $e", e)
            MultiSourceResult.createErrorResult(KildeType.DIGISOS)
        }
    }
}
