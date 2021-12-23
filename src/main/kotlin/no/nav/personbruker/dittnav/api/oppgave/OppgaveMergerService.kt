package no.nav.personbruker.dittnav.api.oppgave

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory

class OppgaveMergerService(
    private val oppgaveService: OppgaveService,
    private val digiSosService: DigiSosService,
    private val unleashService: UnleashService
) {

    private val log = LoggerFactory.getLogger(OppgaveMergerService::class.java)

    suspend fun getActiveEvents(user: AuthenticatedUser): MultiSourceResult<OppgaveDTO, KildeType> = withContext(Dispatchers.IO) {
        val oppgaver = async {
            oppgaveService.getActiveOppgaveEvents(user)
        }

        val ettersendelser = async {
            fetchActiveFromDigiSosIfEnabled(user)
        }

        oppgaver.await() + ettersendelser.await()
    }

    private suspend fun fetchActiveFromDigiSosIfEnabled(user: AuthenticatedUser) =
        if (unleashService.digiSosOppgaveEnabled(user)) {
            log.info("Henter aktive oppgaver for Digisos")
            digiSosService.getEttersendelseActive(user)
        } else {
            MultiSourceResult.createEmptyResult()
        }

    suspend fun getInactiveEvents(user: AuthenticatedUser): MultiSourceResult<OppgaveDTO, KildeType> = withContext(Dispatchers.IO) {
        val oppgaver = async {
            oppgaveService.getInactiveOppgaveEvents(user)
        }

        val ettersendelser = async {
            fetchInactiveFromDigiSosIfEnabled(user)
        }

        oppgaver.await() + ettersendelser.await()
    }

    private suspend fun fetchInactiveFromDigiSosIfEnabled(user: AuthenticatedUser) : MultiSourceResult<OppgaveDTO, KildeType> {
        return if (unleashService.digiSosOppgaveEnabled(user)) {
            log.info("Henter inaktive oppgaver for Digisos")
            digiSosService.getEttersendelseInactive(user)
        } else {
            MultiSourceResult.createEmptyResult()
        }
    }

}
