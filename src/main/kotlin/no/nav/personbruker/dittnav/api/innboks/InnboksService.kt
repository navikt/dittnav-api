package no.nav.personbruker.dittnav.api.innboks

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import org.slf4j.LoggerFactory

class InnboksService (private val innboksConsumer: InnboksConsumer) {

    private val log = LoggerFactory.getLogger(InnboksService::class.java)

    suspend fun getActiveInnboksEvents(innloggetBruker: InnloggetBruker): List<InnboksDTO> {
        return getInnboksEvents(innloggetBruker) {
            innboksConsumer.getExternalActiveEvents(it)
        }
    }

    suspend fun getInactiveInnboksEvents(innloggetBruker: InnloggetBruker): List<InnboksDTO> {
        return getInnboksEvents(innloggetBruker) {
            innboksConsumer.getExternalInactiveEvents(it)
        }
    }

    private suspend fun getInnboksEvents(
            innloggetBruker: InnloggetBruker,
            getEvents: suspend (InnloggetBruker) -> List<Innboks>
    ): List<InnboksDTO> {
        return try {
            val externalEvents = getEvents(innloggetBruker)
            externalEvents.map { innboks -> toInnboksDTO(innboks) }
        } catch (exception: Exception) {
            log.error(exception)
            emptyList()
        }
    }
}
