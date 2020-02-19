package no.nav.personbruker.dittnav.api.brukernotifikasjon

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.personbruker.dittnav.api.beskjed.BeskjedService
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService

class BrukernotifikasjonService(
        private val oppgaveService: OppgaveService,
        private val beskjedService: BeskjedService,
        private val innboksService: InnboksService
) {

    suspend fun getBrukernotifikasjoner(innloggetBruker: InnloggetBruker): List<Brukernotifikasjon> {
        return coroutineScope {
            val oppgaver = async { getOppgaver(innloggetBruker) }
            val beskjed = async { getBeskjed(innloggetBruker) }
            val innboks = async { getInnboks(innloggetBruker) }

            oppgaver.await() + beskjed.await() + innboks.await()
        }
    }

    private suspend fun getOppgaver(innloggetBruker: InnloggetBruker): List<Brukernotifikasjon> {
        return oppgaveService.getOppgaveEventsAsBrukernotifikasjoner(innloggetBruker)
    }

    private suspend fun getBeskjed(innloggetBruker: InnloggetBruker): List<Brukernotifikasjon> {
        return beskjedService.getBeskjedEventsAsBrukernotifikasjoner(innloggetBruker)
    }

    private suspend fun getInnboks(innloggetBruker: InnloggetBruker): List<Brukernotifikasjon> {
        return innboksService.getInnboksEventsAsBrukernotifikasjoner(innloggetBruker)
    }
}
