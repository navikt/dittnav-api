package no.nav.personbruker.dittnav.api.brukernotifikasjon

import io.ktor.http.auth.HttpAuthHeader
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

    fun getBrukernotifikasjoner(authHeader: HttpAuthHeader?): List<Brukernotifikasjon> {
        val brukernotifikasjoner: MutableList<Brukernotifikasjon> = mutableListOf()

        runBlocking {
            val oppgaver = async { getOppgaver(authHeader) }
            val informasjon = async { getInformasjon(authHeader) }
            brukernotifikasjoner.addAll(oppgaver.await())
            brukernotifikasjoner.addAll(informasjon.await())
        }
        return brukernotifikasjoner
    }

    private suspend fun getOppgaver(authHeader: HttpAuthHeader?): List<Brukernotifikasjon> {
        return oppgaveService.getOppgaveEventsAsBrukernotifikasjoner(authHeader)
    }

    private suspend fun getInformasjon(authHeader: HttpAuthHeader?): List<Brukernotifikasjon> {
        return informasjonService.getInformasjonEventsAsBrukernotifikasjoner(authHeader)
    }
}
