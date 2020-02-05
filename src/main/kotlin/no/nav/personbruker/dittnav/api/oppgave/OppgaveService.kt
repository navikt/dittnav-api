package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import org.slf4j.LoggerFactory

class OppgaveService(private val oppgaveConsumer: OppgaveConsumer) {

    private val log = LoggerFactory.getLogger(OppgaveService::class.java)

    suspend fun getOppgaveEventsAsBrukernotifikasjoner(innloggetBruker: InnloggetBruker): List<Brukernotifikasjon> {
        return try {
            oppgaveConsumer.getExternalEvents(innloggetBruker).map { oppgave ->
                 toBrukernotifikasjon(oppgave)
            }
        } catch (exception: Exception) {
            log.error(exception)
            emptyList()
        }
    }
}
