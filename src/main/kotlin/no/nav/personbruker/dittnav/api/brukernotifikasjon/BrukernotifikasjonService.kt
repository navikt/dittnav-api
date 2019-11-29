package no.nav.personbruker.dittnav.api.brukernotifikasjon

import kotlinx.coroutines.*
import no.nav.personbruker.dittnav.api.beskjed.BeskjedService
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import org.slf4j.LoggerFactory

class BrukernotifikasjonService(
        private val oppgaveService: OppgaveService,
        private val beskjedService: BeskjedService,
        private val innboksService: InnboksService
) {

    private val log = LoggerFactory.getLogger(BrukernotifikasjonService::class.java)

    suspend fun getBrukernotifikasjoner(token: String): List<Brukernotifikasjon> {
        return coroutineScope {
            val oppgaver = async(Dispatchers.IO) { getOppgaver(token) }
            val beskjed = async(Dispatchers.IO) { getBeskjed(token) }
            val innboks = async(Dispatchers.IO) { getInnboks(token) }

            oppgaver.await() + beskjed.await() + innboks.await()
        }
    }

    private suspend fun getOppgaver(token: String): List<Brukernotifikasjon> {
        return oppgaveService.getOppgaveEventsAsBrukernotifikasjoner(token)
    }

    private suspend fun getBeskjed(token: String): List<Brukernotifikasjon> {
        return beskjedService.getBeskjedEventsAsBrukernotifikasjoner(token)
    }

    private suspend fun getInnboks(token: String): List<Brukernotifikasjon> {
        return innboksService.getInnboksEventsAsBrukernotifikasjoner(token)
    }
}
