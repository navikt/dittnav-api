package no.nav.personbruker.dittnav.api.brukernotifikasjon

import kotlinx.coroutines.*
import no.nav.personbruker.dittnav.api.informasjon.InformasjonService
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import org.slf4j.LoggerFactory

class BrukernotifikasjonService(
        private val oppgaveService: OppgaveService,
        private val informasjonService: InformasjonService,
        private val innboksService: InnboksService
) {

    private val log = LoggerFactory.getLogger(BrukernotifikasjonService::class.java)

    suspend fun getBrukernotifikasjoner(token: String): List<Brukernotifikasjon> {
        return coroutineScope {
            val oppgaver = async(Dispatchers.IO) { getOppgaver(token) }
            val informasjon = async(Dispatchers.IO) { getInformasjon(token) }
            val innboks = async(Dispatchers.IO) { getInnboks(token) }

            oppgaver.await() + informasjon.await() + innboks.await()
        }
    }

    private suspend fun getOppgaver(token: String): List<Brukernotifikasjon> {
        return oppgaveService.getOppgaveEventsAsBrukernotifikasjoner(token)
    }

    private suspend fun getInformasjon(token: String): List<Brukernotifikasjon> {
        return informasjonService.getInformasjonEventsAsBrukernotifikasjoner(token)
    }

    private suspend fun getInnboks(token: String): List<Brukernotifikasjon> {
        return innboksService.getInnboksEventsAsBrukernotifikasjoner(token)
    }
}
