package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import org.slf4j.LoggerFactory

class OppgaveService(private val oppgaveConsumer: OppgaveConsumer) {

    private val log = LoggerFactory.getLogger(OppgaveService::class.java)

    suspend fun getOppgaveEventsAsBrukernotifikasjoner(token: String): List<Brukernotifikasjon> {
        try {
            oppgaveConsumer.getExternalEvents(token).let {
                return OppgaveTransformer.toBrukernotifikasjonList(it) }
        } catch (exception: Exception) {
            log.error(exception)
        }
        return emptyList()
    }
}
