package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import org.slf4j.LoggerFactory

class OppgaveService(private val oppgaveConsumer: OppgaveConsumer) {

    private val log = LoggerFactory.getLogger(OppgaveService::class.java)

    suspend fun getActiveOppgaveEvents(innloggetBruker: InnloggetBruker): List<OppgaveDTO> {
        return getOppgaveEvents(innloggetBruker) {
            oppgaveConsumer.getExternalActiveEvents(it)
        }
    }

    suspend fun getInactiveOppgaveEvents(innloggetBruker: InnloggetBruker): List<OppgaveDTO> {
        return getOppgaveEvents(innloggetBruker) {
            oppgaveConsumer.getExternalInactiveEvents(it)
        }
    }

    suspend fun getOppgaveEventsAsBrukernotifikasjoner(innloggetBruker: InnloggetBruker): List<Brukernotifikasjon> {
        return try {
            oppgaveConsumer.getExternalActiveEvents(innloggetBruker).map { oppgave ->
                toBrukernotifikasjon(oppgave)
            }
        } catch (exception: Exception) {
            log.error(exception)
            emptyList()
        }
    }

    private suspend fun getOppgaveEvents(
            innloggetBruker: InnloggetBruker,
            getEvents: suspend (InnloggetBruker) -> List<Oppgave>
    ): List<OppgaveDTO> {
        return try {
            val externalEvents = getEvents(innloggetBruker)
            externalEvents.map { oppgave -> toOppgaveDTO(oppgave) }
        } catch (exception: Exception) {
            log.error(exception)
            emptyList()
        }
    }
}
