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
            oppgaveConsumer.getExternalActiveEvents(innloggetBruker)
                    .filter { oppgave -> innloggetBruker.innloggingsnivaa >= oppgave.sikkerhetsnivaa }
                    .map { oppgave -> toBrukernotifikasjon(oppgave) }
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
            externalEvents.map { oppgave -> transformToDTO(oppgave, innloggetBruker) }
        } catch (exception: Exception) {
            log.error(exception)
            emptyList()
        }
    }

    private fun transformToDTO(oppgave: Oppgave, innloggetBruker: InnloggetBruker): OppgaveDTO {
        return if(innloggetBrukerIsAllowedToViewAllDataInEvent(oppgave, innloggetBruker)) {
            toOppgaveDTO(oppgave)
        } else {
            toMaskedOppgaveDTO(oppgave)
        }
    }

    private fun innloggetBrukerIsAllowedToViewAllDataInEvent(oppgave: Oppgave, innloggetBruker: InnloggetBruker): Boolean {
        return innloggetBruker.innloggingsnivaa >= oppgave.sikkerhetsnivaa
    }
}
