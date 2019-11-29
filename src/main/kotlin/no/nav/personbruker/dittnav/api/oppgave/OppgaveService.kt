package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import org.slf4j.LoggerFactory

class OppgaveService(private val oppgaveConsumer: OppgaveConsumer) {

    private val log = LoggerFactory.getLogger(OppgaveService::class.java)

    suspend fun getOppgaveEventsAsBrukernotifikasjoner(token: String): List<Brukernotifikasjon> {
        return try {
            oppgaveConsumer.getExternalEvents(token).map { oppgave ->
                 toBrukernotifikasjon(oppgave)
            }
        } catch (exception: Exception) {
            log.error(exception)
            emptyList()
        }
    }
}
