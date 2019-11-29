package no.nav.personbruker.dittnav.api.brukernotifikasjon

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.personbruker.dittnav.api.beskjed.BeskjedService
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService

class BrukernotifikasjonService(
        private val oppgaveService: OppgaveService,
        private val beskjedService: BeskjedService,
        private val innboksService: InnboksService
) {

    suspend fun getBrukernotifikasjoner(token: String): List<Brukernotifikasjon> {
        return coroutineScope {
            val oppgaver = async { getOppgaver(token) }
            val beskjed = async { getBeskjed(token) }
            val innboks = async { getInnboks(token) }

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
