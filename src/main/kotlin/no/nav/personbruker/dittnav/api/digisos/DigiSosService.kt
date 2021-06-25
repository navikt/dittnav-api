package no.nav.personbruker.dittnav.api.digisos

import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class DigiSosService(private val digiSosConsumer: DigiSosConsumer) {

    private val log = LoggerFactory.getLogger(DigiSosService::class.java)

    suspend fun getActiveEvents(user: AuthenticatedUser): BeskjedResult {
        return fetchEvents(user) { user ->
            val results = digiSosConsumer.getPaabegynte(user)
            results.filter { result ->
                result.synligFremTil.isAfter(LocalDateTime.now())
            }
        }
    }

    suspend fun getInactiveEvents(user: AuthenticatedUser): BeskjedResult {
        return fetchEvents(user) { user ->
            val results = digiSosConsumer.getPaabegynte(user)
            results.filter { result ->
                result.synligFremTil.isBefore(LocalDateTime.now())
            }
        }
    }

    private suspend fun fetchEvents(
        user: AuthenticatedUser,
        getEvents: suspend (AuthenticatedUser) -> List<Paabegynte>
    ): BeskjedResult {
        return try {
            val externalEvents = getEvents(user)
            val results = externalEvents.map { external ->
                external.toInternal()
            }
            BeskjedResult(results)

        } catch (e: Exception) {
            log.warn("Klarte ikke å hente data fra DigiSos: $e", e)
            BeskjedResult(listOf(KildeType.DIGISOS))
        }
    }
}
