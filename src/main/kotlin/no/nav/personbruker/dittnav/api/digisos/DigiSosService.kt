package no.nav.personbruker.dittnav.api.digisos

import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory

class DigiSosService(private val digiSosConsumer: DigiSosConsumer) {

    private val log = LoggerFactory.getLogger(DigiSosService::class.java)

    suspend fun getPaabegynteActive(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        return fetchEvents(user) {
            digiSosConsumer.getPaabegynteActive(user)
        }
    }

    suspend fun getPaabegynteInactive(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        return fetchEvents(user) {
            digiSosConsumer.getPaabegynteInactive(user)
        }
    }

    private suspend fun fetchEvents(
        user: AuthenticatedUser,
        getEvents: suspend (AuthenticatedUser) -> List<Paabegynte>
    ): MultiSourceResult<BeskjedDTO, KildeType> {
        return try {
            val externalEvents = getEvents(user)
            val results = externalEvents.map { external ->
                external.toInternal()
            }
            MultiSourceResult.createSuccessfulResult(results, KildeType.DIGISOS)

        } catch (e: Exception) {
            log.warn("Klarte ikke å hente data fra DigiSos: $e", e)
            MultiSourceResult.createErrorResult(KildeType.DIGISOS)
        }
    }
}
