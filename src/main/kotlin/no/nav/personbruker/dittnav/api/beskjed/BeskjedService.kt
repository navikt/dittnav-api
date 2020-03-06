package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import org.slf4j.LoggerFactory

class BeskjedService(private val beskjedConsumer: BeskjedConsumer) {

    private val log = LoggerFactory.getLogger(BeskjedService::class.java)

    suspend fun getActiveBeskjedEvents(innloggetBruker: InnloggetBruker): List<BeskjedDTO> {
        return getBeskjedEvents(innloggetBruker) {
            beskjedConsumer.getExternalActiveEvents(it)
        }
    }

    suspend fun getInactiveBeskjedEvents(innloggetBruker: InnloggetBruker): List<BeskjedDTO> {
        return getBeskjedEvents(innloggetBruker) {
            beskjedConsumer.getExternalInactiveEvents(it)
        }
    }

    private suspend fun getBeskjedEvents(
            innloggetBruker: InnloggetBruker,
            getEvents: suspend (InnloggetBruker) -> List<Beskjed>
    ): List<BeskjedDTO> {
        return try {
            val externalEvents = getEvents(innloggetBruker)
            externalEvents.map { beskjed -> toBeskjedDTO(beskjed) }
        } catch(exception: Exception) {
            log.error(exception)
            emptyList()
        }
    }
}
