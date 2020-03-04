package no.nav.personbruker.dittnav.api.innboks

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import org.slf4j.LoggerFactory

class InnboksService (private val innboksConsumer: InnboksConsumer) {

    private val log = LoggerFactory.getLogger(InnboksService::class.java)

    suspend fun getInnboksEventsAsBrukernotifikasjoner(innloggetBruker: InnloggetBruker): List<InnboksDTO> {
        return try {
            innboksConsumer.getExternalEvents(innloggetBruker).map { innboks ->
                toInnboksDTO(innboks)
            }
        } catch (exception: Exception) {
            log.error(exception)
            emptyList()
        }
    }
}
