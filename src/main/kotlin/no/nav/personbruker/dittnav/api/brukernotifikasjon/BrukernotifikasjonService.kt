package no.nav.personbruker.dittnav.api.brukernotifikasjon

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.informasjon.InformasjonService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import org.slf4j.LoggerFactory

class BrukernotifikasjonService(
        private val oppgaveService: OppgaveService,
        private val informasjonService: InformasjonService
) {

    private val log = LoggerFactory.getLogger(BrukernotifikasjonService::class.java)

    fun getBrukernotifikasjoner(token: String): List<Brukernotifikasjon> {
        val brukernotifikasjoner: MutableList<Brukernotifikasjon> = mutableListOf()

        runBlocking {
            val oppgaver = async { getOppgaver(token) }
            val informasjon = async { getInformasjon(token) }
            brukernotifikasjoner.addAll(oppgaver.await())
            brukernotifikasjoner.addAll(informasjon.await())
        }
        return brukernotifikasjoner
    }

    private suspend fun getOppgaver(token: String): List<Brukernotifikasjon> {
        return oppgaveService.getOppgaveEventsAsBrukernotifikasjoner(token)
    }

    private suspend fun getInformasjon(token: String): List<Brukernotifikasjon> {
        return informasjonService.getInformasjonEventsAsBrukernotifikasjoner(token)
    }
}
