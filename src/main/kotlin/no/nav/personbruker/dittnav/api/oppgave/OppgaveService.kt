package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import org.slf4j.LoggerFactory

class OppgaveService(private val oppgaveConsumer: OppgaveConsumer) {

    private val log = LoggerFactory.getLogger(OppgaveService::class.java)

    suspend fun getOppgaveEventsAsBrukernotifikasjoner(innloggetBruker: InnloggetBruker): List<OppgaveDTO> {
        return try {
            oppgaveConsumer.getExternalEvents(innloggetBruker).map { oppgave ->
                 toOppgaveDTO(oppgave)
            }
        } catch (exception: Exception) {
            log.error(exception)
            emptyList()
        }
    }
}
